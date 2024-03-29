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
import java.util.ArrayList;
import java.util.List;
/**
 * AccountBean
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-06-01T14:56:15.142+07:00[Asia/Novosibirsk]")
public class AccountBean {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("username")
  private String username = null;

  @SerializedName("password")
  private String password = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("surname")
  private String surname = null;

  /**
   * Gets or Sets roles
   */
  @JsonAdapter(RolesEnum.Adapter.class)
  public enum RolesEnum {
    ADMIN("ROLE_ADMIN"),
    STUDENT("ROLE_STUDENT"),
    TEACHER("ROLE_TEACHER");

    private String value;

    RolesEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RolesEnum fromValue(String input) {
      for (RolesEnum b : RolesEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RolesEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RolesEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public RolesEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RolesEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("roles")
  private List<RolesEnum> roles = null;

  public AccountBean id(Long id) {
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

  public AccountBean username(String username) {
    this.username = username;
    return this;
  }

   /**
   * Get username
   * @return username
  **/
  @Schema(description = "")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public AccountBean password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @Schema(description = "")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AccountBean name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AccountBean surname(String surname) {
    this.surname = surname;
    return this;
  }

   /**
   * Get surname
   * @return surname
  **/
  @Schema(description = "")
  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public AccountBean roles(List<RolesEnum> roles) {
    this.roles = roles;
    return this;
  }

  public AccountBean addRolesItem(RolesEnum rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<RolesEnum>();
    }
    this.roles.add(rolesItem);
    return this;
  }

   /**
   * Get roles
   * @return roles
  **/
  @Schema(description = "")
  public List<RolesEnum> getRoles() {
    return roles;
  }

  public void setRoles(List<RolesEnum> roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountBean accountBean = (AccountBean) o;
    return Objects.equals(this.id, accountBean.id) &&
        Objects.equals(this.username, accountBean.username) &&
        Objects.equals(this.password, accountBean.password) &&
        Objects.equals(this.name, accountBean.name) &&
        Objects.equals(this.surname, accountBean.surname) &&
        Objects.equals(this.roles, accountBean.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, name, surname, roles);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountBean {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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
