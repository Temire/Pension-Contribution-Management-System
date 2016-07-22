package ng.com.justjava.epayment.utility;

import com.etranzact.fundgate.ws.*;




public class FundGateAPITest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //doDailyStatementProcess();
        //doBalanceProcess();
        //doBulkTransferProcess();
       // String h = doMP1();
        //doMP2(h);
        //doFundsTransferProcess();
        //doTransactionStatusProcess();
        doAccountQueryProcess();
        //doBankList();
        //doDailyStatement();
        //doMiniStatement();
        //doPayBills();
    }
    
    
    public static void doMiniStatement()
    {
        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();


            request.setAction("MS");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();
            
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setReference("244996gfc0h8b2uytggg");
            request.setTransaction(t);
            
            

            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result OtherRef = "+result.getOtherReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void doDailyStatement()
    {
        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();


            request.setAction("DS");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();
            
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setStatementDate("2014-09-03");
            t.setReference("244996ggty2uytggg");
            request.setTransaction(t);
            
            

            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result OtherRef = "+result.getOtherReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public static void doBankList()
    {
        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();


            request.setAction("BL");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setReference("2449960827ggg");
            request.setTransaction(t);
            
            

            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result OtherRef = "+result.getOtherReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

 public static void doAccountQueryProcess()
     {
         try
         {
             // Call Web Service Operation

            //XTrustProvider.install();                    
            //FundGateImplService service = new FundGateImplService();            
            System.setProperty("https.protocols", "SSLv3");
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();
            
            request.setAction("AQ");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setDestination("0025840828");
            t.setReference("100dcxssrg5588p6");
            t.setEndPoint("A");
            t.setBankCode("063");
            t.setAmount(0.0);
            request.setTransaction(t);
            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result OtherRef = "+result.getOtherReference());
         }
         catch(Exception ex)
                 {
                     ex.printStackTrace();
                 }
     }


    public static void doTransactionStatusProcess(){

        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();


            request.setAction("TS");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setAmount(0);
            t.setLineType("OTHERS");
            t.setReference("2449960827777566544");
            request.setTransaction(t);
            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result OtherRef = "+result.getOtherReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void doFundsTransferProcess(){

        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();


            request.setAction("FT");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();
            t.setDestination("0025840828");
            t.setEndPoint("A");
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setBankCode("063");
            t.setAmount(10);
            t.setReference("2449960hg7r5655");
            request.setTransaction(t);
            
            /*request.setAction("FT");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();
            t.setDestination("2348033758035");
            t.setEndPoint("M");
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setAmount(10);
            t.setReference("2004996082244475477ll25");
            request.setTransaction(t);*/
            

            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result OtherRef = "+result.getOtherReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void doDailyStatementProcess(){

        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();

            request.setAction("DS");
            request.setTerminalId("7010000002");//20000000054, 7010000002
            Transaction t = new Transaction();
            t.setSource("2348129125926");
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");

            //t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setStatementDate("2014-05-27");
            t.setId("1");
            t.setReference("0929210222791022791");
            request.setTransaction(t);

            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


     public static String doMP1(){
        String m = "";
        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();

            request.setAction("MP");
            request.setTerminalId("7000000001");//20000000054
            Transaction t = new Transaction();
            //t.setSource("2348129125926");
            t.setSource("2348186399749");
            t.setPin("RB7qqya42fKeSoAj0GJFqQ==");
            t.setId("1");
            t.setReference("0929210227791yyyh");
            request.setTransaction(t);

            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            m = result.getMessage();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return m;
    }


    public static void doMP2(String token){
        try { // Call Web Service Operation

            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();

            request.setAction("MP");
            request.setTerminalId("7000000001");//20000000054
            Transaction t = new Transaction();
            //t.setSource("2348129125926");
            t.setSource("2348186399749");
            t.setPin("RB7qqya42fKeSoAj0GJFqQ==");
            t.setDestination("41157294764");
            t.setId("2");
            t.setToken(token);
            t.setLineType("DSTV");
            t.setAmount(1);
            t.setReference("099121c02227391g5hh");
            request.setTransaction(t);

            FundResponse result = port.process(request);

            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void doBalanceProcess(){

        try { // Call Web Service Operation
            //FundGateImplService service = null;
            FundGate port = WebserviceUtil.getPort();

            FundRequest request = new FundRequest();
            request.setAction("BE");
            request.setTerminalId("7000000001");//20000000054
            Transaction t = new Transaction();
            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");//0012
            t.setReference("4529800991027799854340");
            request.setTransaction(t);

            FundResponse result = port.process(request);

            System.out.println("Result Code = "+result);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result Other Ref = "+result.getOtherReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void doBulkTransferProcess(){

        try { // Call Web Service Operation
            //FundGateImplService service = new FundGateImplService();
            FundGate port = WebserviceUtil.getPort();
            FundRequest request = new FundRequest();

            request.setAction("BT");
            request.setTerminalId("7000000001");
            Transaction t = new Transaction();

            t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
            t.setReference("4529800g7998543540");

            t.setAmount(30);//bulk amount
            t.setCompanyId("00000000000000000018");
            t.setSenderName("eTranzact");

            BulkItems bi = new BulkItems();

            BulkItem bit1 = new BulkItem();
            bit1.setBeneficiaryName("Rajesh");
            bit1.setAccountId("205764927266");
            bit1.setAmount(10);
            bit1.setBankCode("033");
            bit1.setUniqueId("41A14AA33q10CE5BB");

            BulkItem bit2 = new BulkItem();
            bit2.setBeneficiaryName("Azuka");
            bit2.setAccountId("2348182108431");
            bit2.setAmount(5);
            bit2.setUniqueId("32A14BA37q1005CEB");

            BulkItem bit3 = new BulkItem();
            bit3.setBeneficiaryName("Peter");
            bit3.setAccountId("2347062988820");
            bit3.setAmount(10);
            bit3.setUniqueId("32A14Bgg10CE5AC");

            bi.getBulkItem().add(bit1);
            bi.getBulkItem().add(bit2);
            bi.getBulkItem().add(bit3);


            //t.setBulkItems(bi);
            request.setTransaction(t);

            FundResponse result = port.process(request);
            System.out.println("Result Code = "+result.getError());
            System.out.println("Result Message = "+result.getMessage());
            System.out.println("Result Ref = "+result.getReference());
            System.out.println("Result OtherRef = "+result.getOtherReference());
            System.out.println("Result Amount = "+result.getAmount());
            System.out.println("Result TotalFailed = "+result.getTotalFailed());
            System.out.println("Result TotalSuccess = "+result.getTotalSuccess());
            System.out.println("Result Company = "+result.getCompanyId());
            System.out.println("Result Action = "+result.getAction());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}