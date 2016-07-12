package com.agiliumlabs.smev.ws.ds;

public interface Consts {

	String SIGNATURE_EXPORT_PRIVATEKEY_CERTIFICATE = System.getProperty("user.dir") + "/data/keystore";
	String SIGNATURE_HEADER_SIGNATURE_TEMPLATE = System.getProperty("user.dir") + "/data/headerSignature.xml";

	String FILE_INPUT_SIMPLE = System.getProperty("user.dir") + "/data/agiliumlabs/toSignSimple.xml";
	String FILE_INPUT_APPDATA = System.getProperty("user.dir") + "/data/agiliumlabs/toSignAppData.xml";
	String FILE_INPUT_ATTACHMENT_INNER = System.getProperty("user.dir") + "/data/agiliumlabs/toSignInnerAttachment.xml";
	String FILE_ARCHIVE_FOLDER = System.getProperty("user.dir") + "/data/agiliumlabs/archive";
	String FILE_ARCHIVE_REQUEST_CODE = "8461cc0a-d350-437f-a9a3-c701b0b338cd";
	String FILE_ATTACHMENT_ZIP = System.getProperty("user.dir") + "/data/agiliumlabs/attachment.zip";
	
	String FILE_SIGNATURE_DSIG_TEMPLATE = System.getProperty("user.dir") + "/data/agiliumlabs/signature_dsig.xml";

	String FILE_WSDL_SMEV = System.getProperty("user.dir") + "/src/wsdl/SID0003022_Copy/smev.gosuslugi.ru.rev120315.xsd";
}
