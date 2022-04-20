/*
 * OpenAPI definition
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package romanow.abc.exam.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * NewMessageBean
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaClientCodegen", date = "2022-04-20T22:54:15.839+07:00[Asia/Novosibirsk]")
public class NewMessageBean {

  @SerializedName("text")
  private String text = null;
  
  @SerializedName("artefactId")
  private Long artefactId = null;
  
  public NewMessageBean text(String text) {
    this.text = text;
    return this;
  }

  
  /**
  * Get text
  * @return text
  **/
  @ApiModelProperty(value = "")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  
  public NewMessageBean artefactId(Long artefactId) {
    this.artefactId = artefactId;
    return this;
  }

  
  /**
  * Get artefactId
  * @return artefactId
  **/
  @ApiModelProperty(value = "")
  public Long getArtefactId() {
    return artefactId;
  }
  public void setArtefactId(Long artefactId) {
    this.artefactId = artefactId;
  }
  
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewMessageBean newMessageBean = (NewMessageBean) o;
    return Objects.equals(this.text, newMessageBean.text) &&
        Objects.equals(this.artefactId, newMessageBean.artefactId);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(text, artefactId);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewMessageBean {\n");
    
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    artefactId: ").append(toIndentedString(artefactId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  
}



