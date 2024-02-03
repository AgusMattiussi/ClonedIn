import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import JobOfferDto from "../utils/JobOfferDto"
import JobOfferDiscoverCard from "../components/cards/jobOfferDiscoverCard"
import Loader from "../components/loader"
import { useEffect, useState } from "react"
import { useSharedAuth } from "../api/auth"
import { useGetJobOfferById } from "../hooks/useGetJobOfferById"
import { useNavigate, useParams } from "react-router-dom"
import { HttpStatusCode } from "axios"

function JobOffer() {
  const navigate = useNavigate()

  const { id } = useParams()
  const { userInfo } = useSharedAuth()

  const { getJobOfferById } = useGetJobOfferById()

  const [job, setJob] = useState<JobOfferDto>({} as JobOfferDto)
  const [isJobLoading, setJobLoading] = useState(true)

  useEffect(() => {
    const fetchJob = async () => {
      const response = await getJobOfferById(id)

      if (response.status === HttpStatusCode.Ok) {
        setJob(response.data)

      }
      else {
        console.error("Error fetching job offer information")
      }
      setJobLoading(false)
    }
    if (isJobLoading) fetchJob()
  }, [getJobOfferById, id, navigate, isJobLoading])

  document.title = job?.position + " | ClonedIn"

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
