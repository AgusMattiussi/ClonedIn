import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import CardHeader from "react-bootstrap/esm/CardHeader"
import { useTranslation } from "react-i18next"

function JobOfferCard({
  enterpriseName,
  category,
  position,
  modality,
  salary,
  skills,
  description,
  contacted,
}: {
  enterpriseName: string
  category: string
  position: string
  modality: string
  salary: string
  skills: string
  description: string
  contacted: boolean
}) {
  const { t } = useTranslation()

  return (
    <Card style={{ marginTop: "5px", marginBottom: "5px", width: "100%" }}>
      <CardHeader className="d-flex justify-content-between align-items-center">
        <div className="d-flex justify-content-start pt-2">
          <h5>
            {enterpriseName} | {position}
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
          <Badge pill bg="success" className="mb-2" style={{ width: "fit-content", margin: "auto" }}>
            Skill
          </Badge>
          <Badge pill bg="success mb-2" style={{ width: "fit-content", margin: "auto" }}>
            Skill
          </Badge>
        </div>
        {contacted ? (
          <div className="d-flex flex-column">
            <h5>
              <Badge bg="secondary">{t("ContactedOrApplied")}</Badge>
            </h5>
          </div>
        ) : (
          <div className="d-flex flex-column"></div>
        )}
      </div>
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Description")}</h5>
        </div>
        <div>
          <Button variant="outline-dark" href="/jobOffer">
            {t("View More")}
          </Button>
        </div>
      </div>
      <div className="d-flex align-items-start flex-wrap px-3">
        <div>
          <p style={{ textAlign: "left", wordBreak: "break-all" }}>{description.substring(0, 200)}</p>
        </div>
      </div>
    </Card>
  )
}

//TODO: ver traduccion de valores por default
JobOfferCard.defaultProps = {
  enterpriseName: "Enterprise",
  category: "No especificado",
  position: "No especificado",
  modality: "No especificado",
  salary: "No especificado",
  skills: "No especificado",
  description:
    "No especificado aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
  contacted: false,
}

export default JobOfferCard
