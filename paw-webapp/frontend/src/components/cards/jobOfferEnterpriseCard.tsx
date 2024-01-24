import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import CardHeader from "react-bootstrap/esm/CardHeader"
import CancelModal from "../modals/cancelModal"
import CategoryDto from "../../utils/CategoryDto"
import SkillDto from "../../utils/SkillDto"
import { useTranslation } from "react-i18next"
import { useRequestApi } from "../../api/apiRequest"
import { useEffect, useState } from "react"
import { HttpStatusCode } from "axios"
import { JobOfferAvailability, UserRole } from "../../utils/constants"
import { Link, useNavigate } from "react-router-dom"
import { useSharedAuth } from "../../api/auth"

function JobOfferEnterpriseCard({
  status,
  contacted,
  job,
  handleClose,
  setJobOfferId,
}: {
  status: any
  contacted: boolean
  job: any
  handleClose: any
  setJobOfferId: any
}) {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { loading, apiRequest } = useRequestApi()
  const { userInfo } = useSharedAuth()

  const [jobCategory, setJobCategory] = useState<CategoryDto | undefined>({} as CategoryDto)
  const [skillsData, setSkillsData] = useState<SkillDto[]>([])
  const [loadingData, setLoadingData] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (loadingData) {
          const [categoryResponse, skillsResponse] = await Promise.all([
            apiRequest({ url: job.links.category, method: "GET" }),
            apiRequest({ url: job.links.skills, method: "GET" }),
          ])

          setJobCategory(categoryResponse.data)
          setSkillsData(skillsResponse.status === HttpStatusCode.NoContent ? [] : skillsResponse.data)
          setLoadingData(false)
        }
      } catch (error) {
        console.error("Error fetching data:", error)
      }
    }
    if (loadingData) fetchData()
  }, [apiRequest])

  const jobOfferSkillsList = skillsData.map((skill) => {
    return (
      <Badge key={skill.id} pill bg="success" className="mx-1">
        {skill.description}
      </Badge>
    )
  })

  return (
    <Card style={{ marginTop: "5px", marginBottom: "5px", width: "100%" }}>
      {" "}
      <CardHeader className="d-flex justify-content-between align-items-center">
        <div className="d-flex justify-content-start pt-2">
          {userInfo?.role === UserRole.ENTERPRISE ? (
            <h5>
            <Link to={`/jobOffers/${job.id}`} style={{ textDecoration: "none" }}>
              {job.position}
            </Link>
            </h5>
            ) : (
              <h5>
              {job.position}
              </h5>
            )}
        </div>
        <span>
          <h5 className="pt-2">
            <Badge pill bg="success">
              {job.links.category == null ? t("No-especificado") : t(jobCategory!.name)}
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
            {jobOfferSkillsList.length === 0 ? <div>{t("Skills Not Specified")}</div> : jobOfferSkillsList}
          </div>
        </div>
      </div>
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Description")}</h5>
        </div>
        {userInfo?.role === UserRole.ENTERPRISE ? (
        job.available === JobOfferAvailability.CLOSED ? (
          <Badge bg="danger" style={{ width: "fit-content", height: "fit-content", padding: "8px" }}>
            {t("Closed")}
          </Badge>
        ) : (
          <div>
            <Button
              variant="outline-dark"
              data-bs-toggle="modal"
              data-bs-target="#cancelModal"
              onClick={() => setJobOfferId(job.id)}
            >
              {t("Close Job Offer")}
            </Button>
            <CancelModal
              title={t("Modal Title")}
              msg={t("Close JobOffer Modal Msg")}
              cancel={t("Cancel")}
              confirm={t("Confirm")}
              onConfirmClick={handleClose}
            />
          </div>
        )
        ) : (
          <div>
            <Button variant="outline-dark" onClick={() => navigate(`/jobOffers/${job.id}`)}>
              {t("View More")}
            </Button>
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
