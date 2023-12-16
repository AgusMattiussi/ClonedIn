import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import JobOfferCard from "../components/cards/jobOfferCard"
import { useEffect, useState } from "react"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import JobOfferDto from "../utils/JobOfferDto"
import { useNavigate, useParams } from "react-router-dom"
import { BASE_URL } from "../utils/constants"

function JobOffer() {
  const { loading, apiRequest } = useRequestApi()
  const [job, setJob] = useState<JobOfferDto | undefined>({} as JobOfferDto)

  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const navigate = useNavigate()

  const USER_API_URL = BASE_URL + `/jobOffer/${id}/`

  useEffect(() => {
    const fetchJob = async () => {
      const response = await apiRequest({
        url: `/jobOffers/${id}`,
        method: "GET",
      })

      if (response.status === 500) {
        navigate("/403")
      }

      setJob(response.data)
    }
      fetchJob()
  }, [apiRequest, id])

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container
        className="p-2 rounded-3 d-flex flex-wrap w-auto justify-content-center"
        fluid
        style={{ background: "#F2F2F2" }}
      >
        <div style={{ maxWidth: "1000px", minHeight: "100vh" }}>
          <JobOfferCard job={job} />
        </div>
      </Container>
    </div>
  )
}

export default JobOffer
