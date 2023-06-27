import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import CardHeader from "react-bootstrap/esm/CardHeader"
import { useTranslation } from "react-i18next"

function JobOfferNotificationsCard({
  enterpriseName,
  category,
  position,
  modality,
  salary,
  skills,
  description,
  date,
  status,
}: {
  enterpriseName: string
  category: string
  position: string
  modality: string
  salary: string
  skills: string
  description: string
  date: string
  status: string
}) {
  const { t } = useTranslation()

  return (
    <Card style={{ marginTop: "5px", marginBottom: "5px", width: "100%" }}>
      <CardHeader className="d-flex justify-content-between align-items-center">
        <div className="d-flex justify-content-start pt-2">
          <h5>
            <a href="/profileEnterprise" style={{ textDecoration: "none" }}>
              {enterpriseName}{" "}
            </a>
            |
            <a href="/enterpriseJobOffer" style={{ textDecoration: "none" }}>
              {" "}
              {position}
            </a>
          </h5>
        </div>
        <span>
          <h5 className="pt-2">
            <Badge pill bg="success">
              {category}
            </Badge>
          </h5>
        </span>
      </CardHeader>
      <br />
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Modality")}</h5>
          <p>{modality}</p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Salary")}</h5>
          <p>
            {salary === "No especificado" ? "" : "$"}
            {salary}
          </p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Required Skills")}</h5>
          <div className="d-flex flex-row justify-content-start">
            <Badge pill bg="success" className="mb-2 mx-2" style={{ width: "fit-content" }}>
              Skill
            </Badge>
            <Badge pill bg="success mb-2" style={{ width: "fit-content" }}>
              Skill
            </Badge>
          </div>
        </div>

        <div className="d-flex flex-column">
          <h5>{t("Date")}</h5>
          <p>{date}</p>
        </div>
        <div className="d-flex flex-column">
          {status !== "pendiente" ? (
            <>
              <h5>
                {t("Status")}
                {": "} {status}
              </h5>
            </>
          ) : (
            <>
              <h5>
                {t("Status")}
                {": "}
              </h5>
              <Button variant="success" style={{ minWidth: "90px", marginBottom: "5px" }}>
                {t("Accept")}
              </Button>
              <Button variant="danger" style={{ minWidth: "90px" }}>
                {t("Decline")}
              </Button>
            </>
          )}
        </div>
        <div className="d-flex flex-column"></div>
      </div>
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Description")}</h5>
        </div>
      </div>
      <div className="d-flex align-items-start flex-wrap px-3">
        <div>
          <p style={{ textAlign: "left", wordBreak: "break-all" }}>
            {description.length > 200 ? description.substring(0, 200) + "..." : description}
          </p>
        </div>
      </div>
    </Card>
  )
}

//TODO: ver traduccion de valores por default
JobOfferNotificationsCard.defaultProps = {
  enterpriseName: "Enterprise",
  category: "No especificado",
  position: "No especificado",
  modality: "No especificado",
  salary: "No especificado",
  skills: "No especificado",
  description:
    "No especificado aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
  date: "No especificado",
  status: "pendiente",
}

export default JobOfferNotificationsCard
