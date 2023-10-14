import * as Icon from "react-bootstrap-icons"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import defaultProfile from "../../images/defaultProfilePicture.png"
import { useTranslation } from "react-i18next"
import { useEffect, useState } from "react"
import GetUserData from "../../utils/userApi"
import CategoryDto from "../../utils/CategoryDto"

function ProfileUserCard({ editable, contacted, user }: { editable: boolean; contacted: boolean; user: any }) {
  const { t } = useTranslation()

  const userSkillsList = GetUserData(user.userSkills).map((skill) => {
    return (
      <Badge pill bg="success" className="mx-2">
        {skill.description}
      </Badge>
    )
  })

  const [userCategory, setUserCategory] = useState<CategoryDto | undefined>({} as CategoryDto)

  useEffect(() => {
    fetch(user.category)
      .then((response) => response.json())
      .then((data) => {
        setUserCategory(data)
      })
      .catch((err) => {
        console.log(err.message)
      })
  }, [])

  return (
    <Card className="profileCard rounded-3 mx-2" style={{ width: "14rem" }}>
      <Card.Img variant="top" src={defaultProfile} />
      {editable ? (
        <Button type="button" variant="success" href="/imageProfile">
          <div className="d-flex align-items-center justify-content-center">
            <Icon.PlusSquare color="white" size={20} style={{ marginRight: "7px" }} />
            {t("Edit Profile Picture")}
          </div>
        </Button>
      ) : (
        <></>
      )}
      <Card.Body style={{ alignContent: "left", alignItems: "left" }}>
        <div className="d-flex justify-content-around align-items-center">
          <h5>{user.name}</h5>
          {editable ? (
            <Button className="float-end" type="button" variant="outline-success" style={{ paddingBottom: "10px" }}>
              <Icon.PencilSquare color="green" size={15} />
            </Button>
          ) : contacted ? (
            <Badge className="p-2" bg="secondary">
              {t("Contacted")}
            </Badge>
          ) : (
            <></>
          )}
        </div>
        {editable ? <hr /> : <></>}
        <Card.Text>
          <div className="d-flex flex-column">
            <div className="d-flex justify-content-start my-2">
              <Icon.ListTask color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              {user.category !== "No-Especificado" ? (
                <div className="d-flex flex-row align-items-center">
                  {t("Category")}:
                  <Badge pill bg="success" className="mx-2" style={{ height: "fit-content" }}>
                    {user.category == null ? t("No especificado") : userCategory?.name}
                  </Badge>
                </div>
              ) : (
                <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                  {t("Category")}: {t("No especificado")}
                </p>
              )}
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Briefcase color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Current Position")}:{" "}
                {user.currentPosition === "" || user.currentPosition == null
                  ? t("No especificado")
                  : user.currentPosition}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Book color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Education Level")}:{" "}
                {user.educationLevel === "" || user.educationLevel == null
                  ? t("No especificado")
                  : t(user.educationLevel)}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.GeoAltFill color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Location")}: {user.location === "" || user.location == null ? t("No especificado") : user.location}
              </p>
            </div>
            <div className="d-flex justify-content-start align-items-center my-2">
              {userSkillsList.length === 0 ? <div>{t("Skills Not Specified")}</div> : <div>{userSkillsList}</div>}
            </div>
          </div>
        </Card.Text>
      </Card.Body>
    </Card>
  )
}

//TODO: ver traduccion de valores por default
ProfileUserCard.defaultProps = {
  editable: false,
  contacted: false,
}

export default ProfileUserCard
