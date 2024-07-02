import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import CardHeader from "react-bootstrap/CardHeader"
import CategoryDto from "../../utils/CategoryDto"
import EnterpriseDto from "../../utils/EnterpriseDto"
import SkillDto from "../../utils/SkillDto"
import JobOfferDto from "../../utils/JobOfferDto"
import { useTranslation } from "react-i18next"
import { useRequestApi } from "../../api/apiRequest"
import { useEffect, useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import { HttpStatusCode } from "axios"
import { useSharedAuth } from "../../api/auth"
import { JobOfferAvailability, UserRole } from "../../utils/constants"
import AcceptModal from "../modals/acceptModal"

function JobOfferDiscoverCard({ seeMoreView, job }: { seeMoreView: boolean; job: JobOfferDto }) {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { apiRequest } = useRequestApi()
  const { userInfo } = useSharedAuth()

  const [jobEnterprise, setJobEnterprise] = useState<EnterpriseDto | undefined>({} as EnterpriseDto)
  const [jobCategory, setJobCategory] = useState<CategoryDto | undefined>({} as CategoryDto)
  const [skillsData, setSkillsData] = useState<SkillDto[]>([])
  const [loadingData, setLoadingData] = useState(true)
  const [error, setError] = useState("")

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (loadingData) {
          const [enterpriseResponse, categoryResponse, skillsResponse] = await Promise.all([
            apiRequest({ url: job.links.enterprise, method: "GET" }),
            apiRequest({ url: job.links.category, method: "GET" }),
            apiRequest({ url: job.links.skills, method: "GET" }),
          ])

          setJobEnterprise(enterpriseResponse.data)
          setJobCategory(categoryResponse.data)
          setSkillsData(skillsResponse.status === HttpStatusCode.NoContent ? [] : skillsResponse.data)
          setLoadingData(false)
        }
      } catch (error) {
        console.error("Error fetching data:", error)
      }
    }
    if (loadingData) fetchData()
  }, [])

  const handleApply = async () => {
    const response = await apiRequest({
      url: `/contacts`,
      method: "POST",
      body: {
        jobOfferId: job.id,
        userId: jobEnterprise?.id
      },
    })

    if (response.status === HttpStatusCode.Created) {
      const modalElement = document.getElementById("acceptModal")
      modalElement?.classList.remove("show")
      document.body.classList.remove("modal-open")
      const modalBackdrop = document.querySelector(".modal-backdrop")
      if (modalBackdrop) {
        modalBackdrop.remove()
      }
      navigate(`/users/${userInfo?.id}/applications`)
    } else if (response.status === HttpStatusCode.Conflict) {
      setError(t("ContactedOrAppliedUser") as string)
      const modalElement = document.getElementById("acceptModal")
      modalElement?.classList.remove("show")
      document.body.classList.remove("modal-open")
      const modalBackdrop = document.querySelector(".modal-backdrop")
      if (modalBackdrop) {
        modalBackdrop.remove()
      }
    }
  }

  const jobOfferSkillsList = skillsData.map((skill, index) => {
    return (
      <Badge key={index} pill bg="success" className="mx-1">
        {skill.description}
      </Badge>
    )
  })

  return (
    <Card style={{ marginTop: "5px", marginBottom: "5px", width: "100%" }}>
      <CardHeader className="d-flex justify-content-between align-items-center">
        <div className="d-flex justify-content-start pt-2">
          {userInfo?.role === UserRole.USER ? (
            <h5>
              <Link to={`/enterprises/${jobEnterprise?.id}`} style={{ textDecoration: "none" }}>
                {jobEnterprise?.name}{" "}
              </Link>
              | {job.position}
            </h5>
          ) : (
            <h5>{job.position}</h5>
          )}
        </div>
        <span>
          <h5 className="pt-2">
            <Badge pill bg="success">
              {jobCategory!.name == "No-Especificado" ? t("No especificado") : t(jobCategory!.name)}
            </Badge>
          </h5>
        </span>
      </CardHeader>
      <br />
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Modality")}</h5>
          <p>{t(job.modality)}</p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Salary")}</h5>
          <p>
            {job.salary == null ? t("Salary Not Specified") : "$"}
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
        {seeMoreView ? (
          job.available === JobOfferAvailability.CLOSED ? (
            <Badge bg="danger" style={{ width: "fit-content", height: "fit-content", padding: "8px" }}>
              {t("Closed")}
            </Badge>
          ) : userInfo?.role === UserRole.USER ? (
            <div>
              <Button
                data-testid="apply-button"
                variant="outline-dark"
                data-bs-toggle="modal"
                data-bs-target="#acceptModal"
              >
                {t("Apply")}
              </Button>
              {error && (
                <div className="error" style={{ color: "red" }}>
                  {error}
                </div>
              )}
            </div>
          ) : (
            <></>
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
        {seeMoreView ? (
          <div>
            <p style={{ textAlign: "left", wordBreak: "break-all" }}>{job.description}</p>
          </div>
        ) : (
          <div>
            <p style={{ textAlign: "left", wordBreak: "break-all" }}>
              {job.description.length > 200 ? job.description.substring(0, 200) + "..." : job.description}
            </p>
          </div>
        )}
      </div>
      <AcceptModal
        title={t("Modal Title")}
        msg={t("Application Modal Msg")}
        cancel={t("Cancel")}
        confirm={t("Confirm")}
        onConfirmClick={handleApply}
      />
    </Card>
  )
}

JobOfferDiscoverCard.defaultProps = {
  seeMoreView: false,
}

export default JobOfferDiscoverCard
