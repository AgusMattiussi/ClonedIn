import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Loader from "../components/loader"

function Loading() {
  document.title = "ClonedIn"

  return (
    <div>
      <Header />
      <Container
        className="p-2 rounded-3 d-flex flex-wrap w-auto justify-content-center"
        fluid
        style={{ background: "#F2F2F2" }}
      >
        <div style={{ width: "90%", height: "100vh" }}>
          <div className="my-5">
            <Loader />
          </div>
        </div>
      </Container>
    </div>
  )
}

export default Loading
