import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import JobOfferDto from "../utils/JobOfferDto"
import JobOfferDiscoverCard from "../components/cards/jobOfferDiscoverCard"
import Loader from "../components/loader"
import { useEffect, useState } from "react"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import { useNavigate, useParams } from "react-router-dom"
import { HttpStatusCode } from "axios"

function JobOffer() {
  const navigate = useNavigate()

  const { id } = useParams()
  const { userInfo } = useSharedAuth()

  const { loading, apiRequest } = useRequestApi()
  const [job, setJob] = useState<JobOfferDto>({} as JobOfferDto)
  const [isJobLoading, setJobLoading] = useState(true)

  useEffect(() => {
    const fetchJob = async () => {
      const response = await apiRequest({
        url: `/jobOffers/${id}`,
        method: "GET",
      })

      if (response.status === HttpStatusCode.InternalServerError || response.status === HttpStatusCode.Forbidden) {
        navigate("/403")
      }

      setJob(response.data)
      setJobLoading(false)
    }
    if (isJobLoading) fetchJob()
  }, [apiRequest, id, navigate, isJobLoading])

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container
        className="p-2 rounded-3 d-flex flex-wrap w-auto justify-content-center"
        fluid
        style={{ background: "#F2F2F2" }}
      >
        <div style={{ width: "90%", height: "100vh" }}>
          {isJobLoading ? (
            <div className="my-5">
              <Loader />
            </div>
          ) : (
            <JobOfferDiscoverCard job={job} seeMoreView={true} />
          )}
        </div>
      </Container>
    </div>
  )
}

export default JobOffer
