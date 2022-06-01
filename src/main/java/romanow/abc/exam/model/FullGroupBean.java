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
import romanow.abc.exam.model.FullGroupRatingBean;
import romanow.abc.exam.model.GroupBean;
import romanow.abc.exam.model.StudentBean;
/**
 * FullGroupBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-06-01T14:56:15.142+07:00[Asia/Novosibirsk]")
public class FullGroupBean {
  @SerializedName("group")
  private GroupBean group = null;

  @SerializedName("students")
  private List<StudentBean> students = null;

  @SerializedName("groupRatings")
  private List<FullGroupRatingBean> groupRatings = null;

  public FullGroupBean group(GroupBean group) {
    this.group = group;
    return this;
  }

   /**
   * Get group
   * @return group
  **/
  @Schema(description = "")
  public GroupBean getGroup() {
    return group;
  }

  public void setGroup(GroupBean group) {
    this.group = group;
  }

  public FullGroupBean students(List<StudentBean> students) {
    this.students = students;
    return this;
  }

  public FullGroupBean addStudentsItem(StudentBean studentsItem) {
    if (this.students == null) {
      this.students = new ArrayList<StudentBean>();
    }
    this.students.add(studentsItem);
    return this;
  }

   /**
   * Get students
   * @return students
  **/
  @Schema(description = "")
  public List<StudentBean> getStudents() {
    return students;
  }

  public void setStudents(List<StudentBean> students) {
    this.students = students;
  }

  public FullGroupBean groupRatings(List<FullGroupRatingBean> groupRatings) {
    this.groupRatings = groupRatings;
    return this;
  }

  public FullGroupBean addGroupRatingsItem(FullGroupRatingBean groupRatingsItem) {
    if (this.groupRatings == null) {
      this.groupRatings = new ArrayList<FullGroupRatingBean>();
    }
    this.groupRatings.add(groupRatingsItem);
    return this;
  }

   /**
   * Get groupRatings
   * @return groupRatings
  **/
  @Schema(description = "")
  public List<FullGroupRatingBean> getGroupRatings() {
    return groupRatings;
  }

  public void setGroupRatings(List<FullGroupRatingBean> groupRatings) {
    this.groupRatings = groupRatings;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FullGroupBean fullGroupBean = (FullGroupBean) o;
    return Objects.equals(this.group, fullGroupBean.group) &&
        Objects.equals(this.students, fullGroupBean.students) &&
        Objects.equals(this.groupRatings, fullGroupBean.groupRatings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group, students, groupRatings);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FullGroupBean {\n");
    
    sb.append("    group: ").append(toIndentedString(group)).append("\n");
    sb.append("    students: ").append(toIndentedString(students)).append("\n");
    sb.append("    groupRatings: ").append(toIndentedString(groupRatings)).append("\n");
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
