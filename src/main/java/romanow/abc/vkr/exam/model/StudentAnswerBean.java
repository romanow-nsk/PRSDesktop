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

package romanow.abc.vkr.exam.model;

import java.util.Objects;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;

/**
 * StudentAnswerBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-06-01T14:56:15.142+07:00[Asia/Novosibirsk]")
public class StudentAnswerBean {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("task")
  private StudentTaskBean task = null;

  @SerializedName("rating")
  private Integer rating = null;

  @SerializedName("studentRatingId")
  private Long studentRatingId = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    NO_ANSWER("NO_ANSWER"),
    IN_PROGRESS("IN_PROGRESS"),
    SENT("SENT"),
    CHECKING("CHECKING"),
    RATED("RATED"),
    NO_RATING("NO_RATING");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StateEnum fromValue(String input) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("state")
  private StateEnum state = null;

  @SerializedName("number")
  private Integer number = null;

  public StudentAnswerBean id(Long id) {
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

  public StudentAnswerBean task(StudentTaskBean task) {
    this.task = task;
    return this;
  }

   /**
   * Get task
   * @return task
  **/
  @Schema(description = "")
  public StudentTaskBean getTask() {
    return task;
  }

  public void setTask(StudentTaskBean task) {
    this.task = task;
  }

  public StudentAnswerBean rating(Integer rating) {
    this.rating = rating;
    return this;
  }

   /**
   * Get rating
   * @return rating
  **/
  @Schema(description = "")
  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public StudentAnswerBean studentRatingId(Long studentRatingId) {
    this.studentRatingId = studentRatingId;
    return this;
  }

   /**
   * Get studentRatingId
   * @return studentRatingId
  **/
  @Schema(description = "")
  public Long getStudentRatingId() {
    return studentRatingId;
  }

  public void setStudentRatingId(Long studentRatingId) {
    this.studentRatingId = studentRatingId;
  }

  public StudentAnswerBean state(StateEnum state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @Schema(description = "")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public StudentAnswerBean number(Integer number) {
    this.number = number;
    return this;
  }

   /**
   * Get number
   * @return number
  **/
  @Schema(description = "")
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudentAnswerBean studentAnswerBean = (StudentAnswerBean) o;
    return Objects.equals(this.id, studentAnswerBean.id) &&
        Objects.equals(this.task, studentAnswerBean.task) &&
        Objects.equals(this.rating, studentAnswerBean.rating) &&
        Objects.equals(this.studentRatingId, studentAnswerBean.studentRatingId) &&
        Objects.equals(this.state, studentAnswerBean.state) &&
        Objects.equals(this.number, studentAnswerBean.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, task, rating, studentRatingId, state, number);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StudentAnswerBean {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    task: ").append(toIndentedString(task)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    studentRatingId: ").append(toIndentedString(studentRatingId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
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