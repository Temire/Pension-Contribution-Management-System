package ng.com.justjava.epayment.Nibss;

import java.util.*;

import javax.xml.bind.annotation.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Payment.Status;

import org.openxava.jpa.*;
import org.openxava.util.*;

@XmlRootElement(name = "PaymentResponse")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "header", "paymentRecord", "hashValue" })
public class NIBSSPaymentResponse {

	private PaymentResponseHeader header;

	private Collection<PaymentInstructionResponse> paymentRecord;

	private String hashValue;

	public PaymentBatch findPaymentBatch() {
		PaymentBatch batch = null;
		String paymentBatchNo = getHeader().getScheduleId().split("_")[1];
		String ownerUniqueId = getHeader().getScheduleId().split("_")[0];
		String ejbQL = "FROM PaymentBatch p WHERE p.batchNumber="
				+ Long.valueOf(paymentBatchNo) + " AND p.owner.uniqueIdentifier='" + ownerUniqueId +"'";
		batch = (PaymentBatch) XPersistence.getManager().createQuery(ejbQL)
				.getSingleResult();
		return batch;
	}

	public void processStatus() {

		PaymentBatch batch = findPaymentBatch();
		batch.setErrorCode(getHeader().getStatus());
		
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The Header Error Code====" + getHeader().getStatus()
				+ " and the batch id ==" + batch.getBatchNumber());
		
		batch.setErrorMessage(NIBSSPaymentResponse.getCodeDescription(getHeader().getStatus()));
		if (Is.equalAsString(getHeader().getStatus(), "00")) {
			batch.setStatus(Status.Paid);
		}
		XPersistence.getManager().merge(batch);
		XPersistence.commit();
		
		if(getPaymentRecord() == null || getPaymentRecord().size()<= 0)
			return;
   
		for (PaymentInstructionResponse instruction : getPaymentRecord()) {
			if (instruction.getNarration() != null
					|| instruction.getNarration().matches("-?\\d+(\\.\\d+)?")) {

				String ejbQL = " FROM PaymentInstruction p WHERE p.id="
						+ Long.valueOf(instruction.getNarration());
				try {
					PaymentInstruction paymentInstruction = (PaymentInstruction) XPersistence
							.getManager().createQuery(ejbQL).getSingleResult();
					if (paymentInstruction != null) {
						paymentInstruction.setFundGateStatus(instruction.getStatus());
						paymentInstruction.setFundGateMessage(NIBSSPaymentResponse.getCodeDescription(instruction.getStatus()));
						XPersistence.getManager().merge(paymentInstruction);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@XmlElement(name = "Header")
	public PaymentResponseHeader getHeader() {
		return header;
	}

	public void setHeader(PaymentResponseHeader header) {
		this.header = header;
	}

	@XmlElement(name = "PaymentRecord")
	public Collection<PaymentInstructionResponse> getPaymentRecord() {
		return paymentRecord;
	}

	public void setPaymentRecord(
			Collection<PaymentInstructionResponse> paymentRecord) {
		this.paymentRecord = paymentRecord;
	}

	@XmlElement(name = "HashValue")
	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
	
	public static String getCodeDescription(String code){
		List<NibbsResponseDescription> codeDecsription= XPersistence.getManager().
				createQuery("FROM NibbsResponseDescription r where r.code='"+code+"'").getResultList();
		if(codeDecsription !=null && !codeDecsription.isEmpty()){
			return codeDecsription.get(0).getDescription();
		}
		return null;
	}

}
