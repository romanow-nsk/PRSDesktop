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
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ExamBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-05-08T18:16:36.953+07:00[Asia/Novosibirsk]")
public class ExamBean {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("examRuleId")
  private Long examRuleId = null;

  @SerializedName("disciplineId")
  private Long disciplineId = null;

  @SerializedName("groupIds")
  private List<Long> groupIds = null;

  public ExamBean id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @Schema(description = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ExamBean examRuleId(Long examRuleId) {
    this.examRuleId = examRuleId;
    return this;
  }

   /**
   * Get examRuleId
   * @return examRuleId
  **/
  @Schema(description = "")
  public Long getExamRuleId() {
    return examRuleId;
  }

  public void setExamRuleId(Long examRuleId) {
    this.examRuleId = examRuleId;
  }

  public ExamBean disciplineId(Long disciplineId) {
    this.disciplineId = disciplineId;
    return this;
  }

   /**
   * Get disciplineId
   * @return disciplineId
  **/
  @Schema(description = "")
  public Long getDisciplineId() {
    return disciplineId;
  }

  public void setDisciplineId(Long disciplineId) {
    this.disciplineId = disciplineId;
  }

  public ExamBean groupIds(List<Long> groupIds) {
    this.groupIds = groupIds;
    return this;
  }

  public ExamBean addGroupIdsItem(Long groupIdsItem) {
    if (this.groupIds == null) {
      this.groupIds = new ArrayList<Long>();
    }
    this.groupIds.add(groupIdsItem);
    return this;
  }

   /**
   * Get groupIds
   * @return groupIds
  **/
  @Schema(description = "")
  public List<Long> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(List<Long> groupIds) {
    this.groupIds = groupIds;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExamBean examBean = (ExamBean) o;
    return Objects.equals(this.id, examBean.id) &&
        Objects.equals(this.examRuleId, examBean.examRuleId) &&
        Objects.equals(this.disciplineId, examBean.disciplineId) &&
        Objects.equals(this.groupIds, examBean.groupIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, examRuleId, disciplineId, groupIds);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExamBean {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    examRuleId: ").append(toIndentedString(examRuleId)).append("\n");
    sb.append("    disciplineId: ").append(toIndentedString(disciplineId)).append("\n");
    sb.append("    groupIds: ").append(toIndentedString(groupIds)).append("\n");
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
