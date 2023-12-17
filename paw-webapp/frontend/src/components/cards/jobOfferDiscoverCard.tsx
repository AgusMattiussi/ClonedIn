import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import CardHeader from "react-bootstrap/esm/CardHeader"
import { useTranslation } from "react-i18next"
import { useRequestApi } from "../../api/apiRequest"
import { useEffect, useState } from "react"
import CategoryDto from "../../utils/CategoryDto"
import EnterpriseDto from "../../utils/EnterpriseDto"

function JobOfferDiscoverCard({ contacted, job }: { contacted: boolean; job: any }) {
  const { t } = useTranslation()
  const { loading, apiRequest } = useRequestApi()
  const [jobEnterprise, setJobEnterprise] = useState<EnterpriseDto | undefined>({} as EnterpriseDto)
  const [jobCategory, setJobCategory] = useState<CategoryDto | undefined>({} as CategoryDto)

  useEffect(() => {
    const fetchEnterprise = async () => {
      const response = await apiRequest({
        url: job.enterprise,
        method: "GET",
      })
      setJobEnterprise(response.data)
    }

    const fetchCategory = async () => {
      const response = await apiRequest({
        url: job.category,
        method: "GET",
      })
      setJobCategory(response.data)
    }

      fetchEnterprise()
      fetchCategory()
  }, [apiRequest])

  return (
    <Card style={{ marginTop: "5px", marginBottom: "5px", width: "100%" }}>
      <CardHeader className="d-flex justify-content-between align-items-center">
        <div className="d-flex justify-content-start pt-2">
          <h5>
            <a href={`/profileEnterprise/${jobEnterprise?.id}`} style={{ textDecoration: "none" }}>
              {jobEnterprise?.name}{" "}
            </a>
            | {job.position}
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
            {job.salary === "No-Especificado" ? "" : "$"}
            {job.salary}
          </p>
        </div>
        <div className="d-flex flex-column">
          <h5>{t("Required Skills")}</h5>
          <div className="d-flex flex-row justify-content-start">
            {job.skills.length === 0 ? <div>{t("Skills Not Specified")}</div> : <div>{job.skills.List}</div>}
          </div>
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
          <Button variant="outline-dark" href={`/jobOffer/${job.id}`}>
            {t("View More")}
          </Button>
        </div>
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

JobOfferDiscoverCard.defaultProps = {
  contacted: false,
}

export default JobOfferDiscoverCard
