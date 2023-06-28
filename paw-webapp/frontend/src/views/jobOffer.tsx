import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import JobOfferEnterpriseCard from "../components/cards/jobOfferEnterpriseCard"
import { useEffect } from "react"

function JobOffer() {
  useEffect(() => {
    document.title = "JobOffer | ClonedIn" // TODO: Add job offer name
  }, [])

  return (
    <div>
      <Navigation isEnterprise={true} />
      <Container
        className="p-2 rounded-3 d-flex flex-wrap w-auto justify-content-center"
        fluid
        style={{ background: "#F2F2F2" }}
      >
        <div style={{ maxWidth: "1000px", minHeight: "100vh" }}>
          <JobOfferEnterpriseCard category="Finance" position="CEO" salary="100000" status="closed" />
        </div>
      </Container>
    </div>
  )
}

export default JobOffer
