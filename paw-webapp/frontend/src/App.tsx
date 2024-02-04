import "./styles/App.css"
import Login from "./views/login"
import DiscoverJobs from "./views/discoverJobs"
import DiscoverProfiles from "./views/discoverProfiles"
import RegisterEnterprise from "./views/enterpriseRegister"
import EditEnterpriseForm from "./views/enterpriseEditForm"
import ContactForm from "./views/enterpriseContactForm"
import JobOfferForm from "./views/enterpriseJobOfferForm"
import ImageProfileForm from "./views/imageProfileForm"
import RegisterUser from "./views/userRegister"
import EditUserForm from "./views/userEditForm"
import SkillsForm from "./views/userSkillsForm"
import ExperienceForm from "./views/userExperienceForm"
import EducationForm from "./views/userEducationForm"
import ProfileUser from "./views/userProfile"
import NotificationsUser from "./views/userNotifications"
import ApplicationsUser from "./views/userApplications"
import ProfileEnterprise from "./views/enterpriseProfile"
import ContactsEnterprise from "./views/enterpriseContacts"
import InterestedEnterprise from "./views/enterpriseInterested"
import JobOffer from "./views/jobOffer"
import Error from "./views/error"
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"
import { HttpStatusCode } from "axios"

// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css"
// Bootstrap Bundle JS
import "bootstrap/dist/js/bootstrap.bundle.min"
import { useSharedAuth } from "./api/auth"

function App() {
  const { userInfo } = useSharedAuth()

  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          {userInfo === null ? (
          <Route path="/" element={<Navigate to="/login" />} />
          ) : (
            userInfo?.role === "ENTERPRISE" ? (
              <Route path="/" element={<Navigate to="/users" />} />
            ) : (
              <Route path="/" element={<Navigate to="/jobOffers" />} />
            )
          )}

          {/* ENTERPRISE CONTROLLER */}
          <Route path="/registerEnterprise" element={<RegisterEnterprise />} />
          <Route path="/editEnterprise/:id" element={<EditEnterpriseForm />} />
          <Route path="/enterprises/:id" element={<ProfileEnterprise />} />
          <Route path="/enterprises/:id/image" element={<ImageProfileForm />} />
          <Route path="/enterprises/:id/jobOffers" element={<JobOfferForm />} />
          <Route path="/enterprises/:id/contacts" element={<ContactsEnterprise />} />
          <Route path="/enterprises/:id/interested" element={<InterestedEnterprise />} />
          <Route path="/enterprises/:id/contacts/:userId" element={<ContactForm />} />

          {/* JOB OFFER CONTROLLER */}
          <Route path="/jobOffers" element={<DiscoverJobs />} />
          <Route path="/jobOffers/:id" element={<JobOffer />} />

          {/* USER CONTROLLER */}
          <Route path="/registerUser" element={<RegisterUser />} />
          <Route path="/editUser/:id" element={<EditUserForm />} />
          <Route path="/users" element={<DiscoverProfiles />} />
          <Route path="/users/:id" element={<ProfileUser />} />
          <Route path="/users/:id/skills" element={<SkillsForm />} />
          <Route path="/users/:id/experiences" element={<ExperienceForm />} />
          <Route path="/users/:id/educations" element={<EducationForm />} />
          <Route path="/users/:id/image" element={<ImageProfileForm />} />
          <Route path="/users/:id/notifications" element={<NotificationsUser />} />
          <Route path="/users/:id/applications" element={<ApplicationsUser />} />

          {/* ERROR */}
          <Route path="/401" element={<Error statusCode={HttpStatusCode.Unauthorized} />} />
          <Route path="/403" element={<Error statusCode={HttpStatusCode.Forbidden} />} />
          <Route path="/500" element={<Error statusCode={HttpStatusCode.InternalServerError} />} />
          <Route path="*" element={<Error statusCode={HttpStatusCode.NotFound} />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
