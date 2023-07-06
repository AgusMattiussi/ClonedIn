import * as Icon from "react-bootstrap-icons"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import defaultProfile from "../../images/defaultProfilePicture.png"
import { useTranslation } from "react-i18next"

function ProfileUserCard({
  name,
  category,
  position,
  educationLevel,
  location,
  skills,
  editable,
  contacted,
}: {
  name: string
  category: string
  position: string
  educationLevel: string
  location: string
  skills: string
  editable: boolean
  contacted: boolean
}) {
  const { t } = useTranslation()

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
          <h5>{name}</h5>
          {editable ? (
            <Button className="float-end" type="button" variant="outline-success" href="/editUser" style={{ paddingBottom: "10px" }}>
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
              {category !== "No especificado" ? (
                <div>
                  {t("Job Category")}:
                  <Badge pill bg="success" className="mx-2">
                    {category}
                  </Badge>
                </div>
              ) : (
                <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                  {t("Job Category")}: {category}
                </p>
              )}
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Briefcase color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Current Position")}: {position}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Book color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Education Level")}: {educationLevel}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.GeoAltFill color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Location")}: {location}
              </p>
            </div>
            <div className="d-flex justify-content-start align-items-center my-2">
              {editable ? (
                <></>
              ) : (
                <div>
                  <Badge pill bg="success" className="mx-2">
                    skill1
                  </Badge>
                  <Badge pill bg="success" className="mx-2">
                    skill2
                  </Badge>
                  <Badge pill bg="success" className="mx-2">
                    skill3
                  </Badge>
                </div>
              )}
            </div>
          </div>
        </Card.Text>
      </Card.Body>
    </Card>
  )
}

//TODO: ver traduccion de valores por default
ProfileUserCard.defaultProps = {
  name: "Username",
  category: "No especificado",
  position: "No",
  educationLevel: "No especificado",
  location: "No especificado",
  skills: "No especificado",
  editable: false,
  contacted: false,
}

export default ProfileUserCard
