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
/**
 * ExamPeriodBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-05-08T18:16:36.953+07:00[Asia/Novosibirsk]")
public class ExamPeriodBean {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("start")
  private Long start = null;

  @SerializedName("end")
  private Long end = null;

  @SerializedName("examId")
  private Long examId = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    REDACTION("REDACTION"),
    ALLOWANCE("ALLOWANCE"),
    READY("READY"),
    PROGRESS("PROGRESS"),
    FINISHED("FINISHED"),
    CLOSED("CLOSED");

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

  public ExamPeriodBean id(Long id) {
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

  public ExamPeriodBean start(Long start) {
    this.start = start;
    return this;
  }

   /**
   * Get start
   * @return start
  **/
  @Schema(description = "")
  public Long getStart() {
    return start;
  }

  public void setStart(Long start) {
    this.start = start;
  }

  public ExamPeriodBean end(Long end) {
    this.end = end;
    return this;
  }

   /**
   * Get end
   * @return end
  **/
  @Schema(description = "")
  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
    this.end = end;
  }

  public ExamPeriodBean examId(Long examId) {
    this.examId = examId;
    return this;
  }

   /**
   * Get examId
   * @return examId
  **/
  @Schema(description = "")
  public Long getExamId() {
    return examId;
  }

  public void setExamId(Long examId) {
    this.examId = examId;
  }

  public ExamPeriodBean state(StateEnum state) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExamPeriodBean examPeriodBean = (ExamPeriodBean) o;
    return Objects.equals(this.id, examPeriodBean.id) &&
        Objects.equals(this.start, examPeriodBean.start) &&
        Objects.equals(this.end, examPeriodBean.end) &&
        Objects.equals(this.examId, examPeriodBean.examId) &&
        Objects.equals(this.state, examPeriodBean.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, start, end, examId, state);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExamPeriodBean {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    examId: ").append(toIndentedString(examId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
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
