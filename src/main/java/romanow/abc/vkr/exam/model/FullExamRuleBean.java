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

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * FullExamRuleBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-06-01T14:56:15.142+07:00[Asia/Novosibirsk]")
public class FullExamRuleBean {
  @SerializedName("examRule")
  private ExamRuleBean examRule = null;

  @SerializedName("discipline")
  private FullDisciplineBean discipline = null;

  @SerializedName("themes")
  private List<FullThemeBean> themes = null;

  public FullExamRuleBean examRule(ExamRuleBean examRule) {
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

  public FullExamRuleBean discipline(FullDisciplineBean discipline) {
    this.discipline = discipline;
    return this;
  }

   /**
   * Get discipline
   * @return discipline
  **/
  @Schema(description = "")
  public FullDisciplineBean getDiscipline() {
    return discipline;
  }

  public void setDiscipline(FullDisciplineBean discipline) {
    this.discipline = discipline;
  }

  public FullExamRuleBean themes(List<FullThemeBean> themes) {
    this.themes = themes;
    return this;
  }

  public FullExamRuleBean addThemesItem(FullThemeBean themesItem) {
    if (this.themes == null) {
      this.themes = new ArrayList<FullThemeBean>();
    }
    this.themes.add(themesItem);
    return this;
  }

   /**
   * Get themes
   * @return themes
  **/
  @Schema(description = "")
  public List<FullThemeBean> getThemes() {
    return themes;
  }

  public void setThemes(List<FullThemeBean> themes) {
    this.themes = themes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FullExamRuleBean fullExamRuleBean = (FullExamRuleBean) o;
    return Objects.equals(this.examRule, fullExamRuleBean.examRule) &&
        Objects.equals(this.discipline, fullExamRuleBean.discipline) &&
        Objects.equals(this.themes, fullExamRuleBean.themes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(examRule, discipline, themes);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FullExamRuleBean {\n");
    
    sb.append("    examRule: ").append(toIndentedString(examRule)).append("\n");
    sb.append("    discipline: ").append(toIndentedString(discipline)).append("\n");
    sb.append("    themes: ").append(toIndentedString(themes)).append("\n");
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
