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
 * TaskBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-05-08T18:16:36.953+07:00[Asia/Novosibirsk]")
public class TaskBean {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("text")
  private String text = null;

  @SerializedName("artefactId")
  private Long artefactId = null;

  /**
   * Gets or Sets taskType
   */
  @JsonAdapter(TaskTypeEnum.Adapter.class)
  public enum TaskTypeEnum {
    QUESTION("QUESTION"),
    EXERCISE("EXERCISE");

    private String value;

    TaskTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static TaskTypeEnum fromValue(String input) {
      for (TaskTypeEnum b : TaskTypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<TaskTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TaskTypeEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public TaskTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return TaskTypeEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("taskType")
  private TaskTypeEnum taskType = null;

  @SerializedName("themeId")
  private Long themeId = null;

  public TaskBean id(Long id) {
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

  public TaskBean text(String text) {
    this.text = text;
    return this;
  }

   /**
   * Get text
   * @return text
  **/
  @Schema(description = "")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public TaskBean artefactId(Long artefactId) {
    this.artefactId = artefactId;
    return this;
  }

   /**
   * Get artefactId
   * @return artefactId
  **/
  @Schema(description = "")
  public Long getArtefactId() {
    return artefactId;
  }

  public void setArtefactId(Long artefactId) {
    this.artefactId = artefactId;
  }

  public TaskBean taskType(TaskTypeEnum taskType) {
    this.taskType = taskType;
    return this;
  }

   /**
   * Get taskType
   * @return taskType
  **/
  @Schema(description = "")
  public TaskTypeEnum getTaskType() {
    return taskType;
  }

  public void setTaskType(TaskTypeEnum taskType) {
    this.taskType = taskType;
  }

  public TaskBean themeId(Long themeId) {
    this.themeId = themeId;
    return this;
  }

   /**
   * Get themeId
   * @return themeId
  **/
  @Schema(description = "")
  public Long getThemeId() {
    return themeId;
  }

  public void setThemeId(Long themeId) {
    this.themeId = themeId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaskBean taskBean = (TaskBean) o;
    return Objects.equals(this.id, taskBean.id) &&
        Objects.equals(this.text, taskBean.text) &&
        Objects.equals(this.artefactId, taskBean.artefactId) &&
        Objects.equals(this.taskType, taskBean.taskType) &&
        Objects.equals(this.themeId, taskBean.themeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, artefactId, taskType, themeId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaskBean {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    artefactId: ").append(toIndentedString(artefactId)).append("\n");
    sb.append("    taskType: ").append(toIndentedString(taskType)).append("\n");
    sb.append("    themeId: ").append(toIndentedString(themeId)).append("\n");
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
