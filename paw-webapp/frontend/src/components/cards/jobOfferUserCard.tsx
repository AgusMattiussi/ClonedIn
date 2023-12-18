import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import CardHeader from "react-bootstrap/esm/CardHeader"
import { useTranslation } from "react-i18next"
import CancelModal from "../modals/cancelModal"
import { useRequestApi } from "../../api/apiRequest"
import EnterpriseDto from "../../utils/EnterpriseDto"
import { useEffect, useState } from "react"
import JobOfferDto from "../../utils/JobOfferDto"
import CategoryDto from "../../utils/CategoryDto"

function JobOfferUserCard({ job }: { job: any }) {
  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()
  const [enterprise, setEnterprise] = useState<EnterpriseDto | undefined>({} as EnterpriseDto)
  const [enterpriseLoading, setEnterpriseLoading] = useState(true)
  const [jobOffer, setJobOffer] = useState<JobOfferDto | undefined>({} as JobOfferDto)
  const [jobLoading, setJobLoading] = useState(true)
  const [jobCategory, setJobCategory] = useState<CategoryDto | undefined>({} as CategoryDto)
  const [categoryLoading, setCategoryLoading] = useState(true)


  useEffect(() => {
    const fetchEnterprise = async () => {
      const response = await apiRequest({
        url: job.enterprise,
        method: "GET",
      })
      setEnterprise(response.data)
      setEnterpriseLoading(false)
    }

    const fetchJobOffer = async () => {
      const response = await apiRequest({
        url: job.jobOffer,
        method: "GET",
      })
      setJobOffer(response.data)
      setJobLoading(false)
    }

    const fetchCategory = async () => {
      const response = await apiRequest({
        url: job.jobOffer.category,
        method: "GET",
      })
      setJobCategory(response.data)
      setCategoryLoading(false)
    }

    if (enterpriseLoading === true) {
      fetchEnterprise()
    }
    if (jobLoading === true) {
      fetchJobOffer()
    }
    if (categoryLoading === true) {
      fetchCategory()
    }
    
  }, [apiRequest])

  return (
    <Card style={{ marginTop: "5px", marginBottom: "5px", width: "100%" }}>
      <CardHeader className="d-flex justify-content-between align-items-center">
        <div className="d-flex justify-content-start pt-2">
          <h5>
            <a href={`/profileEnterprise/${enterprise?.id}`} style={{ textDecoration: "none" }}>
              {enterprise?.name}{" "}
            </a>
            |
            <a href={`/jobOffers/${jobOffer?.id}`} style={{ textDecoration: "none" }}>
              {" "}
              {jobOffer?.position}
            </a>
          </h5>
        </div>
        <span>
          <h5 className="pt-2">
            <Badge pill bg="success">
            {jobCategory == null ? t("No-especificado") : jobCategory?.name}
            </Badge>
          </h5>
        </span>
      </CardHeader>
      <br />
      <div className="d-flex justify-content-between px-3">
        <div className="d-flex flex-column">
          <h5>{t("Modality")}</h5>
          <p>{jobOffer?.modality}</p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Salary")}</h5>
          <p>
            {jobOffer?.salary === "No-Especificado" ? "" : "$"}
            {jobOffer?.salary}
          </p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Required Skills")}</h5>
          <div className="d-flex flex-row justify-content-start">
          <Badge pill bg="success" className="mx-2">
            {jobOffer?.skills.length === 0 ? <div>{t("Skills Not Specified")}</div> : <div>{jobOffer?.skills}</div>}
          </Badge>
          </div>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Date")}</h5>
          <p>{job.date}</p>
        </div>
        <div className="d-flex flex-column">
          {job.status !== "pendiente" ? (
            <>
              <h5>
                {t("Status")}
                {": "} {job.status}
              </h5>
            </>
          ) : (
            <>
              <h5>
                {t("Status")}
                {": "} {job.status}
              </h5>
              <Button
                variant="danger"
                style={{ minWidth: "90px" }}
                data-bs-toggle="modal"
                data-bs-target="#cancelModal"
              >
                {t("Cancel")}
              </Button>
              <CancelModal
                title={t("Modal Title")}
                msg={t("Cancel Application Modal Msg")}
                cancel={t("Cancel")}
                confirm={t("Confirm")}
              />
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
            {/* {jobOffer?.description.length > 200 ? jobOffer?.description.substring(0, 200) + "..." : jobOffer?.description} */}
            {jobOffer?.description.substring(0, 200)}
          </p>
        </div>
      </div>
    </Card>
  )
}

export default JobOfferUserCard
