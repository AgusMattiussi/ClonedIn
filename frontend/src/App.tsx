import "./styles/App.css"
import Login from "./views/login"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import DiscoverJobs from "./views/discoverJobs"
import DiscoverProfiles from "./views/discoverProfiles"
import RegisterUser from "./views/registerUser"
import EditUser from "./views/editUser"
import RegisterEnterprise from "./views/registerEnterprise"
import EditEnterprise from "./views/editEnterprise"
import ImageProfile from "./views/imageProfile"
import SkillsUser from "./views/skillsUser"
import ExperienceUser from "./views/experienceUser"
import EducationUser from "./views/educationUser"
import ContactForm from "./views/contactForm"
import JobOfferEnterprise from "./views/jobOfferEnterprise"
import ProfileUser from "./views/profileUser"
import NotificationsUser from "./views/notificationsUser"
import ApplicationsUser from "./views/applicationsUser"

// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css"
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min"
import { Disc } from "react-bootstrap-icons"


function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          {/*TODO: cada link tine que ser unico => agregar el id de cada usuario al path*/}
          <Route path="/login" element={<Login />} />
          <Route path="/registerUser" element={<RegisterUser />} />
          <Route path="/registerEnterprise" element={<RegisterEnterprise />} />
          <Route path="/discoverJobs" element={<DiscoverJobs />} />
          <Route path="/editUser" element={<EditUser />} />
          <Route path="/editEnterprise" element={<EditEnterprise />} />
          <Route path="/imageProfile" element={<ImageProfile />} />
          {/*TODO: revisar --> distinto link para enterprise y user */}

          <Route path="/discoverProfiles" element={<DiscoverProfiles />} />

          <Route path="/profileUser" element={<ProfileUser />} />
          <Route path="/notificationsUser" element={<NotificationsUser />} />
          <Route path="/applicationsUser" element={<ApplicationsUser />} />

          <Route path="/addSkill" element={<SkillsUser />} />
          <Route path="/addExperience" element={<ExperienceUser />} />
          <Route path="/addEducation" element={<EducationUser />} />
          <Route path="/contactUser" element={<ContactForm />} />
          <Route path="/addJobOffer" element={<JobOfferEnterprise />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
