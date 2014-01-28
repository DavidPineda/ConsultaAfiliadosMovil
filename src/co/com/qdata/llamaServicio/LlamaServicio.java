package co.com.qdata.llamaServicio;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LlamaServicio {

	private String NameSpace;
	private String SoapAction;
	private String Method;
	private String url;
	
	public String getNameSpace() {
		return NameSpace;
	}
	public void setNameSpace(String nameSpace) {
		NameSpace = nameSpace;
	}
	public String getSoapAction() {
		return SoapAction;
	}
	public void setSoapAction(String soapAction) {
		SoapAction = soapAction;
	}
	public String getMethod() {
		return Method;
	}
	public void setMethod(String method) {
		Method = method;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public LlamaServicio(String NameSpace, String SoapAction, String Method, String url){
		this.NameSpace = NameSpace;
		this.SoapAction = SoapAction;
		this.Method = Method;
		this.url = url;
	}
	
	public SoapPrimitive llamaServicioPrimitive(PropertyInfo[] propiedades) throws Exception{
		SoapPrimitive primitive = null;
		try{
			SoapObject request = new SoapObject(this.NameSpace, this.Method);
			
			for (PropertyInfo propertyInfo : propiedades) {
				request.addProperty(propertyInfo);
			}
	
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
			
			//Se crea la variable de transporte que se encarga de traer la respuesta de el servicio web
			androidHttpTransport.call(SoapAction, envelope);
			primitive = (SoapPrimitive) envelope.getResponse();
			
		}
		catch (Exception ex) {
			throw ex;
		}
		
		return primitive;
	}
	
}
