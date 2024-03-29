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
 * StudentExamInfoBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-06-01T14:56:15.142+07:00[Asia/Novosibirsk]")
public class StudentExamInfoBean {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("semesterRating")
  private Integer semesterRating = null;

  @SerializedName("questionRating")
  private Integer questionRating = null;

  @SerializedName("exerciseRating")
  private Integer exerciseRating = null;

  @SerializedName("examId")
  private Long examId = null;

  @SerializedName("studentId")
  private Long studentId = null;

  @SerializedName("groupRatingId")
  private Long groupRatingId = null;

  /**
   * Gets or Sets studentRatingState
   */
  @JsonAdapter(StudentRatingStateEnum.Adapter.class)
  public enum StudentRatingStateEnum {
    EMPTY("EMPTY"),
    NOT_ALLOWED("NOT_ALLOWED"),
    ALLOWED("ALLOWED"),
    ASSIGNED_TO_EXAM("ASSIGNED_TO_EXAM"),
    WAITING_TO_APPEAR("WAITING_TO_APPEAR"),
    ABSENT("ABSENT"),
    PASSING("PASSING"),
    FINISHED("FINISHED"),
    RATED("RATED");

    private String value;

    StudentRatingStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StudentRatingStateEnum fromValue(String input) {
      for (StudentRatingStateEnum b : StudentRatingStateEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StudentRatingStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StudentRatingStateEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public StudentRatingStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StudentRatingStateEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("studentRatingState")
  private StudentRatingStateEnum studentRatingState = null;

  @SerializedName("exam")
  private ExamBean exam = null;

  @SerializedName("examRule")
  private ExamRuleBean examRule = null;

  @SerializedName("teacher")
  private TeacherBean teacher = null;

  public StudentExamInfoBean id(Long id) {
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

  public StudentExamInfoBean semesterRating(Integer semesterRating) {
    this.semesterRating = semesterRating;
    return this;
  }

   /**
   * Get semesterRating
   * @return semesterRating
  **/
  @Schema(description = "")
  public Integer getSemesterRating() {
    return semesterRating;
  }

  public void setSemesterRating(Integer semesterRating) {
    this.semesterRating = semesterRating;
  }

  public StudentExamInfoBean questionRating(Integer questionRating) {
    this.questionRating = questionRating;
    return this;
  }

   /**
   * Get questionRating
   * @return questionRating
  **/
  @Schema(description = "")
  public Integer getQuestionRating() {
    return questionRating;
  }

  public void setQuestionRating(Integer questionRating) {
    this.questionRating = questionRating;
  }

  public StudentExamInfoBean exerciseRating(Integer exerciseRating) {
    this.exerciseRating = exerciseRating;
    return this;
  }

   /**
   * Get exerciseRating
   * @return exerciseRating
  **/
  @Schema(description = "")
  public Integer getExerciseRating() {
    return exerciseRating;
  }

  public void setExerciseRating(Integer exerciseRating) {
    this.exerciseRating = exerciseRating;
  }

  public StudentExamInfoBean examId(Long examId) {
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

  public StudentExamInfoBean studentId(Long studentId) {
    this.studentId = studentId;
    return this;
  }

   /**
   * Get studentId
   * @return studentId
  **/
  @Schema(description = "")
  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public StudentExamInfoBean groupRatingId(Long groupRatingId) {
    this.groupRatingId = groupRatingId;
    return this;
  }

   /**
   * Get groupRatingId
   * @return groupRatingId
  **/
  @Schema(description = "")
  public Long getGroupRatingId() {
    return groupRatingId;
  }

  public void setGroupRatingId(Long groupRatingId) {
    this.groupRatingId = groupRatingId;
  }

  public StudentExamInfoBean studentRatingState(StudentRatingStateEnum studentRatingState) {
    this.studentRatingState = studentRatingState;
    return this;
  }

   /**
   * Get studentRatingState
   * @return studentRatingState
  **/
  @Schema(description = "")
  public StudentRatingStateEnum getStudentRatingState() {
    return studentRatingState;
  }

  public void setStudentRatingState(StudentRatingStateEnum studentRatingState) {
    this.studentRatingState = studentRatingState;
  }

  public StudentExamInfoBean exam(ExamBean exam) {
    this.exam = exam;
    return this;
  }

   /**
   * Get exam
   * @return exam
  **/
  @Schema(description = "")
  public ExamBean getExam() {
    return exam;
  }

  public void setExam(ExamBean exam) {
    this.exam = exam;
  }

  public StudentExamInfoBean examRule(ExamRuleBean examRule) {
    this.examRule = examRule;
    return this;
  }

   /**
   * Get examRule
   * @return examRule
  **/
  @Schema(description = "")
  public ExamRuleBean getExamRule() {
    return examRule;
  }

  public void setExamRule(ExamRuleBean examRule) {
    this.examRule = examRule;
  }

  public StudentExamInfoBean teacher(TeacherBean teacher) {
    this.teacher = teacher;
    return this;
  }

   /**
   * Get teacher
   * @return teacher
  **/
  @Schema(description = "")
  public TeacherBean getTeacher() {
    return teacher;
  }

  public void setTeacher(TeacherBean teacher) {
    this.teacher = teacher;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudentExamInfoBean studentExamInfoBean = (StudentExamInfoBean) o;
    return Objects.equals(this.id, studentExamInfoBean.id) &&
        Objects.equals(this.semesterRating, studentExamInfoBean.semesterRating) &&
        Objects.equals(this.questionRating, studentExamInfoBean.questionRating) &&
        Objects.equals(this.exerciseRating, studentExamInfoBean.exerciseRating) &&
        Objects.equals(this.examId, studentExamInfoBean.examId) &&
        Objects.equals(this.studentId, studentExamInfoBean.studentId) &&
        Objects.equals(this.groupRatingId, studentExamInfoBean.groupRatingId) &&
        Objects.equals(this.studentRatingState, studentExamInfoBean.studentRatingState) &&
        Objects.equals(this.exam, studentExamInfoBean.exam) &&
        Objects.equals(this.examRule, studentExamInfoBean.examRule) &&
        Objects.equals(this.teacher, studentExamInfoBean.teacher);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, semesterRating, questionRating, exerciseRating, examId, studentId, groupRatingId, studentRatingState, exam, examRule, teacher);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StudentExamInfoBean {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    semesterRating: ").append(toIndentedString(semesterRating)).append("\n");
    sb.append("    questionRating: ").append(toIndentedString(questionRating)).append("\n");
    sb.append("    exerciseRating: ").append(toIndentedString(exerciseRating)).append("\n");
    sb.append("    examId: ").append(toIndentedString(examId)).append("\n");
    sb.append("    studentId: ").append(toIndentedString(studentId)).append("\n");
    sb.append("    groupRatingId: ").append(toIndentedString(groupRatingId)).append("\n");
    sb.append("    studentRatingState: ").append(toIndentedString(studentRatingState)).append("\n");
    sb.append("    exam: ").append(toIndentedString(exam)).append("\n");
    sb.append("    examRule: ").append(toIndentedString(examRule)).append("\n");
    sb.append("    teacher: ").append(toIndentedString(teacher)).append("\n");
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
