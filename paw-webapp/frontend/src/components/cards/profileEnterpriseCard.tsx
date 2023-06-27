import * as Icon from "react-bootstrap-icons"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import defaultProfile from "../../images/defaultProfilePicture.png"
import { useTranslation } from "react-i18next"

function ProfileEnterpriseCard({
  name,
  category,
  location,
  employees,
  foundationYear,
  website,
  aboutUs,
  editable,
}: {
  name: string
  category: string
  location: string
  employees: string
  foundationYear: string
  website: string
  aboutUs: string
  editable: boolean
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
            <Button
              className="float-end"
              type="button"
              variant="outline-success"
              href="/editEnterprise"
              style={{ paddingBottom: "10px" }}
            >
              <Icon.PencilSquare color="green" size={15} />
            </Button>
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
              <Icon.PeopleFill color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Quantity of employees")}: {employees}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.CalendarEvent color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Funding Year")}: {foundationYear}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Globe color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Website")}: {website}
              </p>
            </div>
            <div className="d-flex flex-column align-items-start my-2">
              <p className="fw-bold" style={{ marginBottom: "2px" }}>
                {t("About Us")}
              </p>
              <p> {aboutUs}</p>
            </div>
          </div>
        </Card.Text>
      </Card.Body>
    </Card>
  )
}

//TODO: ver traduccion de valores por default
ProfileEnterpriseCard.defaultProps = {
  name: "Enterprise",
  category: "No especificado",
  employees: "No especificado",
  foundationYear: "No especificado",
  location: "No especificado",
  website: "No especificado",
  aboutUs: "No especificado",
  editable: false,
}

export default ProfileEnterpriseCard
