import "./styles/App.css"
import Login from "./views/login"
import DiscoverJobs from "./views/discoverJobs"
import DiscoverProfiles from "./views/discoverProfiles"
import RegisterEnterprise from "./views/enterpriseRegister"
import EditEnterprise from "./views/enterpriseEdit"
import ContactForm from "./views/enterpriseContactForm"
import JobOfferEnterprise from "./views/enterpriseJobOffer"
import ImageProfile from "./views/imageProfile"
import RegisterUser from "./views/userRegister"
import EditUser from "./views/userEdit"
import SkillsUser from "./views/userSkills"
import ExperienceUser from "./views/userExperience"
import EducationUser from "./views/userEducation"
import ProfileUser from "./views/userProfile"
import NotificationsUser from "./views/userNotifications"
import ApplicationsUser from "./views/userApplications"
import { BrowserRouter, Routes, Route } from "react-router-dom"

// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css"
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min"

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
