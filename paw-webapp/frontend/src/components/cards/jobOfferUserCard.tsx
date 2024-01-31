import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import CardHeader from "react-bootstrap/esm/CardHeader"
import JobOfferDto from "../../utils/JobOfferDto"
import EnterpriseDto from "../../utils/EnterpriseDto"
import CategoryDto from "../../utils/CategoryDto"
import ContactDto from "../../utils/ContactDto"
import SkillDto from "../../utils/SkillDto"
import AcceptModal from "../modals/acceptModal"
import RejectModal from "../modals/rejectModal"
import CancelModal from "../modals/cancelModal"
import { Link } from "react-router-dom"
import { useTranslation } from "react-i18next"
import { useRequestApi } from "../../api/apiRequest"
import { useEffect, useState } from "react"
import { HttpStatusCode } from "axios"
import { JobOfferStatus } from "../../utils/constants"

function JobOfferUserCard({
  contact,
  job,
  handler,
  setJobOfferId,
  applicationsView,
}: {
  contact: ContactDto
  job: JobOfferDto
  handler: any
  setJobOfferId: any
  applicationsView: boolean
}) {
  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()

  const [jobEnterprise, setJobEnterprise] = useState<EnterpriseDto | undefined>({} as EnterpriseDto)
  const [jobCategory, setJobCategory] = useState<CategoryDto | undefined>({} as CategoryDto)
  const [skillsData, setSkillsData] = useState<SkillDto[]>([])

  const [loadingData, setLoadingData] = useState(true)

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
  }, [apiRequest])

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
          <h5>
            <Link to={`/enterprises/${jobEnterprise?.id}`} style={{ textDecoration: "none" }}>
              {jobEnterprise?.name}{" "}
            </Link>
            |
            <Link to={`/jobOffers/${job?.id}`} style={{ textDecoration: "none" }}>
              {" "}
              {job?.position}
            </Link>
          </h5>
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
          <p>{t(job?.modality)}</p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Salary")}</h5>
          <p>
            {job?.salary === "No-Especificado" ? "" : "$"}
            {job?.salary}
          </p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Required Skills")}</h5>
          <div className="d-flex flex-row justify-content-start">
              {jobOfferSkillsList.length === 0 ? <div>{t("Skills Not Specified")}</div> : jobOfferSkillsList}
          </div>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Date")}</h5>
          <p>{contact.date}</p>
        </div>
        <div className="d-flex flex-column">
          <h5>
            {t("Status")}
            {": "} {t(contact.status)}
          </h5>
          {contact.status !== JobOfferStatus.PENDING ? (
            <></>
          ) : applicationsView ? (
            <Button
              variant="danger"
              style={{ minWidth: "90px" }}
              data-bs-toggle="modal"
              data-bs-target="#cancelModal"
              onClick={() => setJobOfferId(job.id)}
            >
              {t("Cancel")}
            </Button>
          ) : (
            <div className="d-flex flex-column">
              <Button
                variant="success"
                style={{ minWidth: "90px", marginBottom: "5px" }}
                data-bs-toggle="modal"
                data-bs-target="#acceptModal"
                onClick={() => setJobOfferId(job.id)}
              >
                {t("Accept")}
              </Button>
              <Button
                variant="danger"
                style={{ minWidth: "90px", marginBottom: "5px" }}
                data-bs-toggle="modal"
                data-bs-target="#rejectModal"
                onClick={() => setJobOfferId(job.id)}
              >
                {t("Decline")}
              </Button>
            </div>
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
          <p style={{ textAlign: "left", wordBreak: "break-all" }}>{job?.description.substring(0, 200)}</p>
        </div>
      </div>
      <AcceptModal
        title={t("Modal Title")}
        msg={t("Accept Application Modal Msg")}
        cancel={t("Cancel")}
        confirm={t("Confirm")}
        onConfirmClick={() => handler("Accept")}
      />
      <RejectModal
        title={t("Modal Title")}
        msg={t("Reject Application Modal Msg")}
        cancel={t("Cancel")}
        confirm={t("Confirm")}
        onConfirmClick={() => handler("Decline")}
      />
      <CancelModal
        title={t("Modal Title")}
        msg={t("Cancel Application Modal Msg")}
        cancel={t("Cancel")}
        confirm={t("Confirm")}
        onConfirmClick={() => handler()}
      />
    </Card>
  )
}

export default JobOfferUserCard
