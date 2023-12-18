import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import CardHeader from "react-bootstrap/esm/CardHeader"
import { useTranslation } from "react-i18next"
import CancelModal from "../modals/cancelModal"
import { useRequestApi } from "../../api/apiRequest"
import { useEffect, useState } from "react"
import CategoryDto from "../../utils/CategoryDto"

function JobOfferEnterpriseCard({ status, contacted, job }: { status: any; contacted: boolean; job: any }) {
  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()

  const [jobCategory, setJobCategory] = useState<CategoryDto | undefined>({} as CategoryDto)
  const [categoryLoading, setCategoryLoading] = useState(true)

  const [skillsData, setSkillsData] = useState<any[]>([])

  useEffect(() => {
    const fetchCategory = async () => {
      const response = await apiRequest({
        url: job.category,
        method: "GET",
      })
      setJobCategory(response.data)
      setSkillsData(job.skills)
      setCategoryLoading(false)
    }

    if (categoryLoading === true) {
      fetchCategory()
    }
  }, [apiRequest])

  const jobOfferSkillsList = skillsData.map((skill, index) => {
    return (
      <Badge key={index} pill bg="success" className="mx-1">
        {skill}
      </Badge>
    )
  })

  return (
    <Card style={{ marginTop: "5px", marginBottom: "5px", width: "100%" }}>
      {" "}
      <CardHeader className="d-flex justify-content-between align-items-center">
        <div className="d-flex justify-content-start pt-2">
          <h5>
            <a href={`/jobOffers/${job.id}`} style={{ textDecoration: "none" }}>
              {job.position}
            </a>
          </h5>
        </div>
        <span>
          <h5 className="pt-2">
            <Badge pill bg="success">
              {job.category == null ? t("No-especificado") : jobCategory?.name}
            </Badge>
          </h5>
        </span>
      </CardHeader>
      <br />
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Modality")}</h5>
          <p>{job.modality}</p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Salary")}</h5>
          <p>
            {job.salary === "No especificado" ? "" : "$"}
            {job.salary}
          </p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Required Skills")}</h5>
          <div className="d-flex flex-row justify-content-start">
            {job.skills.length === 0 ? <div>{t("Skills Not Specified")}</div> : jobOfferSkillsList}
          </div>
        </div>
      </div>
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Description")}</h5>
        </div>
        {status === "closed" ? (
          <Badge bg="danger" style={{ width: "fit-content", height: "fit-content", padding: "8px" }}>
            {t("Closed")}
          </Badge>
        ) : (
          <div>
            <Button variant="outline-dark" data-bs-toggle="modal" data-bs-target="#cancelModal">
              {t("Close Job Offer")}
            </Button>
            <CancelModal
              title={t("Modal Title")}
              msg={t("Close JobOffer Modal Msg")}
              cancel={t("Cancel")}
              confirm={t("Confirm")}
            />
          </div>
        )}
      </div>
      <div className="d-flex align-items-start flex-wrap px-3">
        <div>
          <p style={{ textAlign: "left", wordBreak: "break-all" }}>
            {job.description.length > 200 ? job.description.substring(0, 200) + "..." : job.description}
          </p>
        </div>
      </div>
    </Card>
  )
}

JobOfferEnterpriseCard.defaultProps = {
  contacted: false,
  status: "closed",
}

export default JobOfferEnterpriseCard
