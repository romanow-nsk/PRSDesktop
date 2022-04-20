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
import romanow.abc.exam.model.Sort;

/**
 * PageableObject
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaClientCodegen", date = "2022-04-20T22:54:15.839+07:00[Asia/Novosibirsk]")
public class PageableObject {

  @SerializedName("sort")
  private Sort sort = null;
  
  @SerializedName("offset")
  private Long offset = null;
  
  @SerializedName("pageNumber")
  private Integer pageNumber = null;
  
  @SerializedName("pageSize")
  private Integer pageSize = null;
  
  @SerializedName("paged")
  private Boolean paged = null;
  
  @SerializedName("unpaged")
  private Boolean unpaged = null;
  
  public PageableObject sort(Sort sort) {
    this.sort = sort;
    return this;
  }

  
  /**
  * Get sort
  * @return sort
  **/
  @ApiModelProperty(value = "")
  public Sort getSort() {
    return sort;
  }
  public void setSort(Sort sort) {
    this.sort = sort;
  }
  
  public PageableObject offset(Long offset) {
    this.offset = offset;
    return this;
  }

  
  /**
  * Get offset
  * @return offset
  **/
  @ApiModelProperty(value = "")
  public Long getOffset() {
    return offset;
  }
  public void setOffset(Long offset) {
    this.offset = offset;
  }
  
  public PageableObject pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  
  /**
  * Get pageNumber
  * @return pageNumber
  **/
  @ApiModelProperty(value = "")
  public Integer getPageNumber() {
    return pageNumber;
  }
  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }
  
  public PageableObject pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  
  /**
  * Get pageSize
  * @return pageSize
  **/
  @ApiModelProperty(value = "")
  public Integer getPageSize() {
    return pageSize;
  }
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }
  
  public PageableObject paged(Boolean paged) {
    this.paged = paged;
    return this;
  }

  
  /**
  * Get paged
  * @return paged
  **/
  @ApiModelProperty(value = "")
  public Boolean isPaged() {
    return paged;
  }
  public void setPaged(Boolean paged) {
    this.paged = paged;
  }
  
  public PageableObject unpaged(Boolean unpaged) {
    this.unpaged = unpaged;
    return this;
  }

  
  /**
  * Get unpaged
  * @return unpaged
  **/
  @ApiModelProperty(value = "")
  public Boolean isUnpaged() {
    return unpaged;
  }
  public void setUnpaged(Boolean unpaged) {
    this.unpaged = unpaged;
  }
  
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageableObject pageableObject = (PageableObject) o;
    return Objects.equals(this.sort, pageableObject.sort) &&
        Objects.equals(this.offset, pageableObject.offset) &&
        Objects.equals(this.pageNumber, pageableObject.pageNumber) &&
        Objects.equals(this.pageSize, pageableObject.pageSize) &&
        Objects.equals(this.paged, pageableObject.paged) &&
        Objects.equals(this.unpaged, pageableObject.unpaged);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(sort, offset, pageNumber, pageSize, paged, unpaged);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageableObject {\n");
    
    sb.append("    sort: ").append(toIndentedString(sort)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    paged: ").append(toIndentedString(paged)).append("\n");
    sb.append("    unpaged: ").append(toIndentedString(unpaged)).append("\n");
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



