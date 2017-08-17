package com.databank.gh.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.databank.gh.model.CustDetails;
import com.databank.gh.model.ProductXPrice;
import com.databank.gh.model.TransDetails;

@Repository
public class BalanceCheckerMethods extends JdbcDaoSupport implements IBalanceCheckerMethods{
	
	
	@Autowired	
	public BalanceCheckerMethods(DataSource dataSource){
		this.setDataSource(dataSource);
	}
	
	@Override
	public ArrayList<CustDetails> getCustDetails(String searchString) {

		String sql = "WITH KeyPersonnel \nAS \n(\n	SELECT ROW_NUMBER() OVER (PARTITION BY HCK.Client_Code ORDER BY HCK.Client_Code, HCK.PersonnelCode) AS RowID, HCK.*\n	FROM [Hdr_ClientHeadKeyPersonnel] HCK WITH (NOLOCK)\n)\n, ClientDocument\nAS\n(\n	SELECT ROW_NUMBER() OVER (PARTITION BY HDOC.[UploadDocID] ORDER BY HDOC.[UploadDocID], HDM.[DocumentCode]) AS RowID\n		, HDOC.[UploadDocID]\n		, HDM.[ExternalMapCode] AS [DocumentMapCode]\n		, DDOC.[ExpiryDate]\n	FROM [Hdr_Upload_Documents] HDOC WITH (NOLOCK)\n	INNER JOIN [Dtl_Upload_Documents] DDOC WITH (NOLOCK)\n		ON DDOC.[UploadDocID] = HDOC.[UploadDocID]\n	INNER JOIN [Hdr_Documents] HDM WITH (NOLOCK)\n		ON HDM.[DocumentCode] = DDOC.[DocumentCode]\n	WHERE HDOC.[DocType] = 1\n)\n\nSELECT top 100 ACC.[CreatedBy] AS [INPUTTER]\n	, ACC.Sebiregno AS DOC_REF,ACC.[CreatedON] AS [INP_DT_TM]\n	, CLI.[UCC] AS NRIC_OLD\n	, COALESCE(ACC.[MapCode], '') AS FHR_REF_NO\n\n	\n	, CLI.[Client_Code] AS CLI_CLIENTCODE\n	, ACC.[Client_Code] AS ACC_CLIENTCODE,ACC.[MAPINCODE]\n	,ACC.CLIENT_FNAME ACC_NAME, COALESCE(CLI.[client_FName], '') + ' ' + COALESCE(CLI.[client_SName], '') + ' ' + COALESCE(CLI.[client_TName], '') AS FIRST_HOLDER_NAME\n	, COALESCE(ACC.[AlrtEmail], '') AS EMAIL_ID\n	, COALESCE(CLI.[res_tel], '') AS TELEPHONE_OFF\n	, COALESCE(CLI.[Off_Tel], '') AS TELEPHONE_RES\n	, COALESCE(BRANCH.[BranchName], '') AS SERVICE_CENTER\n	, \n		CASE \n			WHEN ACC.[MfHolding] = 'SI'\n				THEN 'Single'\n			WHEN ACC.[MfHolding] = 'JO'\n				THEN 'Joint'\n			ELSE COALESCE(ACC.[MfHolding], '')\n		END AS HOLDING_TYPE\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN JH1.[Client_Code] \n			ELSE HCK_1.[Personnelcode]\n		END AS JNT_CIF_REF_NO\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN COALESCE(JH1.[ClientName], '') \n			ELSE COALESCE(HCK_1.[NameOfPerson], '')\n		END AS JOINT_HOLDER_NAME\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN COALESCE(JH1.[AlrtEmail], '') \n			ELSE COALESCE(HCK_1.[EmailID], '')\n		END AS JOINT_HOLDER_EMAILID\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN COALESCE(JH1.[res_tel], '') \n			ELSE COALESCE(HCK_1.[Contactno], '')\n		END AS JOINT_HOLDER_TELEPHONE_OFF\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN COALESCE(JH1.[Off_Tel], '') \n			ELSE ''\n		END AS JOINT_HOLDER_TELEPHONE_RES\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN JH2.[Client_Code] \n			ELSE HCK_2.[Personnelcode]\n		END AS THIRD_CIF_REF_NO\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN COALESCE(JH2.[ClientName], '') \n			ELSE COALESCE(HCK_2.[NameOfPerson], '')\n		END AS THIRD_JOINT_HOLDER_NAME\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN JH3.[Client_Code] \n			ELSE HCK_3.[Personnelcode]\n		END AS FOURTH_CIF_REF_NO\n	, \n		CASE \n			WHEN STA.[indcorporate] = 'I'\n				THEN COALESCE(JH3.[ClientName], '') \n			ELSE COALESCE(HCK_3.[NameOfPerson], '')\n		END AS FOURTH_JOINT_HOLDER_NAME\n	,COALESCE(acc.res_tel,'') RESID_TEL, COALESCE(ACC.MOBILE_NO,'') MOBILE2, COALESCE(CLI.[mobile_no], '') AS MOBILE \n	, COALESCE(CLI.ADDRESS1, '') AS [ADDRESS1]\n	, COALESCE(CLI.[city], '') + ', ' + COALESCE(CLI.[state], '') AS ADDRESS2\n	, COALESCE(CLI.[COUNTRY], '') AS ADDRESS3\n	, COALESCE(CLI.[CtPersonOperation], '') AS MAIDEN_NAME\n	, DOC_1.[ExpiryDate] AS ID_EXPIRY_DATE\n	, COALESCE(DOC_1.[DocumentMapCode], '') AS IDENT_NO\n	, DOC_2.[ExpiryDate] AS ID_EXPIRY_DATE_SECOND\n	, COALESCE(DOC_2.[DocumentMapCode], '') AS IDENT_NO_SECOND\n	, DOC_3.[ExpiryDate] AS ID_EXPIRY_DATE_THIRD\n	, COALESCE(DOC_3.[DocumentMapCode], '') AS IDENT_NO_THIRD\n	, '' AS REMARKS\n\nFROM [dbo].[Hdr_ClientHead] CLI WITH (NOLOCK)\n	INNER JOIN [dbo].[Hdr_Client] ACC WITH (NOLOCK)\n		ON ACC.[Head_ClientCode] = CLI.[Client_Code]\n	INNER JOIN [dbo].[Hdr_Client_Status] STA WITH (NOLOCK)\n		ON STA.[Status_Code] = CLI.[Status_Code]\n\n	LEFT JOIN [dbo].[Hdr_UserBranch] BRANCH WITH (NOLOCK)\n		ON BRANCH.[BranchCode] = ACC.[BranchCode]\n\n	LEFT JOIN [dbo].[DTL_ClientIndividual] DCI WITH (NOLOCK)\n		ON DCI.[ClientCode] = ACC.[Client_Code]\n	LEFT JOIN [Hdr_ClientHead] JH1 WITH (NOLOCK)\n		ON JH1.[Client_Code] = DCI.[Jh1ClientCode]\n	LEFT JOIN [Hdr_ClientHead] JH2 WITH (NOLOCK)\n		ON JH2.[Client_Code] = DCI.[Jh2ClientCode]\n	LEFT JOIN [Hdr_ClientHead] JH3 WITH (NOLOCK)\n		ON JH3.[Client_Code] = DCI.[Jh3ClientCode]\n\n	LEFT JOIN (\n		SELECT *\n		FROM KeyPersonnel HCK\n		WHERE HCK.RowID = 1\n	) HCK_1 ON HCK_1.[Client_Code] = CLI.[Client_Code]\n	LEFT JOIN (\n		SELECT *\n		FROM KeyPersonnel HCK\n		WHERE HCK.RowID = 2\n	) HCK_2 ON HCK_2.[Client_Code] = CLI.[Client_Code]\n	LEFT JOIN (\n		SELECT *\n		FROM KeyPersonnel HCK\n		WHERE HCK.RowID = 3\n	) HCK_3 ON HCK_3.[Client_Code] = CLI.[Client_Code]\n\n	LEFT JOIN (\n		SELECT *\n		FROM ClientDocument CD\n		WHERE CD.RowID = 1\n	) DOC_1 ON DOC_1.[UploadDocID] = CLI.[DocumentID]\n	LEFT JOIN (\n		SELECT *\n		FROM ClientDocument CD\n		WHERE CD.RowID = 2\n	) DOC_2 ON DOC_2.[UploadDocID] = CLI.[DocumentID]\n	LEFT JOIN (\n		SELECT *\n		FROM ClientDocument CD\n		WHERE CD.RowID = 3\n	) DOC_3 ON DOC_3.[UploadDocID] = CLI.[DocumentID]"
				+ searchString;

		ArrayList<CustDetails> allCust = new ArrayList<CustDetails>();

		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

		for (Map row : rows) {
			CustDetails custInfo = new CustDetails();
			custInfo.setName((String) row.get("ACC_NAME"));  
			custInfo.setIssAcc((String) row.get("NRIC_OLD"));  //changed from MAPINCODE. THE UCC IS THE RIGHT APPROACH
			custInfo.setMilesAcc((String) row.get("FHR_REF_NO"));
			custInfo.setMobilePhone((String) row.get("MOBILE")+" ,"+(String)row.get("RESID_TEL")+(String)row.get("TELEPHONE_RES"));
			custInfo.setAddress1((String) row.get("ADDRESS1"));
			custInfo.setAddress2((String) row.get("ADDRESS2"));
			custInfo.setEmail((String) row.get("EMAIL_ID"));
			custInfo.setAddress3((String) row.get("ADDRESS3"));
			custInfo.setTelResidential((String) row.get("TELEPHONE_RES"));
			custInfo.setMaidenName((String)row.get("MAIDEN_NAME"));
			custInfo.setDocRef((String)row.get("DOC_REF"));
			allCust.add(custInfo);
		}

		return allCust;
	}

	@Override
	public ArrayList<TransDetails> getAccountTransactions(String accountNumber) {
		//get price list
		HashMap<String,ProductXPrice> priceList = this.getCurrentPriceList();
				
		String sql = "SELECT ACC.[MapCode] ACCOUNTNO\n		, SCH.[PLANID],\n		\n		\n		SUM(\n		CASE WHEN MUT.TYPE='B' THEN MUT.QTY\n		ELSE (-MUT.QTY)  END\n		\n		) UNITS\n		\n	 \n	\n\nFROM [dbo].[Mutual_Trans] MUT WITH (NOLOCK)\n	INNER JOIN [dbo].[SchemeData] SCH WITH (NOLOCK) \n		ON SCH.[mf_SchCode] = MUT.[scheme_code]\n	INNER JOIN [dbo].[Hdr_Client] ACC WITH (NOLOCK) \n		ON ACC.[Client_code] = MUT.[Client_Code]\n\n\nwhere ACC.[mapcode] = ?\nGROUP BY\n	 ACC.[MapCode]\n	\n	, SCH.[PlanId]";

		ArrayList<TransDetails> allTransDetails = new ArrayList<TransDetails>();

		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql,accountNumber);
		
		for (Map row :rows){
			TransDetails custTrans = new TransDetails();
			custTrans.setUnits((BigDecimal)row.get("UNITS"));
			custTrans.setMutualFund((String)row.get("PLANID"));
			custTrans.setBalance(custTrans.getUnits().multiply(priceList.get(custTrans.getMutualFund()).getPrice()) );
			
			custTrans.setUnits(custTrans.getUnits().setScale(2,BigDecimal.ROUND_HALF_UP));		
 			custTrans.setBalance(custTrans.getBalance().setScale(2,BigDecimal.ROUND_HALF_UP));
 			
 			
 			
 					
			allTransDetails.add(custTrans);
			
		}
		

		return allTransDetails;
	}

	
	@Override
	public HashMap<String, ProductXPrice> getCurrentPriceList() {
		String sql = "WITH MaxDateNav\nAS\n(\n	SELECT Max(NAVDATE) As NavDate\n		, SCH.[Mf_SchCode]\n		, SCH.[PlanID]\n	FROM [dbo].[dbd_Price] PRICE\n		INNER JOIN [dbo].[SchemeData] SCH\n			ON SCH.[mf_schcode] = PRICE.[MF_SCHCODE] \n	GROUP BY SCH.[Mf_schCode]\n	, SCH.[PlanID]\n)\nSELECT NAV.[PlanId] AS [FundID]\n	, PRICE.[NAVRS]	AS [Price]\n	, NAV.NavDate\nFROM [dbo].[dbd_Price] PRICE\n	INNER JOIN MaxDateNav NAV\n		ON NAV.[Mf_Schcode] = PRICE.[MF_SCHCODE] \n		AND PRICE.[NavDate] = NAV.[NavDate] 	";
		//ArrayList<ProductXPrice> priceList = new ArrayList<ProductXPrice>();
		HashMap<String,ProductXPrice> priceList = new HashMap<String,ProductXPrice>();
		List<Map<String,Object>> rows = getJdbcTemplate().queryForList(sql);
		for (Map row:rows){
			ProductXPrice pp =new ProductXPrice();
			pp.setFundId((String)row.get("FundID"));
			pp.setPrice((BigDecimal)row.get("Price"));
			pp.setNavDate((Date)row.get("NavDate"));
			priceList.put(pp.getFundId(), pp);	
			
		}
	
		return priceList;
	}
}
