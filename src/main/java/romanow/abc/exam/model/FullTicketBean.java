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
import romanow.abc.exam.model.FullAnswerBean;
import romanow.abc.exam.model.FullStudentBean;
import romanow.abc.exam.model.TicketBean;
/**
 * FullTicketBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-05-08T18:16:36.953+07:00[Asia/Novosibirsk]")
public class FullTicketBean {
  @SerializedName("ticket")
  private TicketBean ticket = null;

  @SerializedName("answers")
  private List<FullAnswerBean> answers = null;

  @SerializedName("student")
  private FullStudentBean student = null;

  public FullTicketBean ticket(TicketBean ticket) {
    this.ticket = ticket;
    return this;
  }

   /**
   * Get ticket
   * @return ticket
  **/
  @Schema(description = "")
  public TicketBean getTicket() {
    return ticket;
  }

  public void setTicket(TicketBean ticket) {
    this.ticket = ticket;
  }

  public FullTicketBean answers(List<FullAnswerBean> answers) {
    this.answers = answers;
    return this;
  }

  public FullTicketBean addAnswersItem(FullAnswerBean answersItem) {
    if (this.answers == null) {
      this.answers = new ArrayList<FullAnswerBean>();
    }
    this.answers.add(answersItem);
    return this;
  }

   /**
   * Get answers
   * @return answers
  **/
  @Schema(description = "")
  public List<FullAnswerBean> getAnswers() {
    return answers;
  }

  public void setAnswers(List<FullAnswerBean> answers) {
    this.answers = answers;
  }

  public FullTicketBean student(FullStudentBean student) {
    this.student = student;
    return this;
  }

   /**
   * Get student
   * @return student
  **/
  @Schema(description = "")
  public FullStudentBean getStudent() {
    return student;
  }

  public void setStudent(FullStudentBean student) {
    this.student = student;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FullTicketBean fullTicketBean = (FullTicketBean) o;
    return Objects.equals(this.ticket, fullTicketBean.ticket) &&
        Objects.equals(this.answers, fullTicketBean.answers) &&
        Objects.equals(this.student, fullTicketBean.student);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticket, answers, student);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FullTicketBean {\n");
    
    sb.append("    ticket: ").append(toIndentedString(ticket)).append("\n");
    sb.append("    answers: ").append(toIndentedString(answers)).append("\n");
    sb.append("    student: ").append(toIndentedString(student)).append("\n");
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
