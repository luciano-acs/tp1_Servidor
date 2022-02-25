
package Servidor;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SolicitarAutorizacionResult" type="{http://schemas.datacontract.org/2004/07/SGE.Service.Contracts.Data}Autorizacion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "solicitarAutorizacionResult"
})
@XmlRootElement(name = "SolicitarAutorizacionResponse")
public class SolicitarAutorizacionResponse {

    @XmlElementRef(name = "SolicitarAutorizacionResult", namespace = "http://ISTP1.Service.Contracts.Service", type = JAXBElement.class, required = false)
    protected JAXBElement<Autorizacion> solicitarAutorizacionResult;

    /**
     * Obtiene el valor de la propiedad solicitarAutorizacionResult.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Autorizacion }{@code >}
     *     
     */
    public JAXBElement<Autorizacion> getSolicitarAutorizacionResult() {
        return solicitarAutorizacionResult;
    }

    /**
     * Define el valor de la propiedad solicitarAutorizacionResult.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Autorizacion }{@code >}
     *     
     */
    public void setSolicitarAutorizacionResult(JAXBElement<Autorizacion> value) {
        this.solicitarAutorizacionResult = value;
    }

}
