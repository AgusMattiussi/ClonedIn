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
import { BrowserRouter, Routes, Route } from "react-router-dom"
import { HttpStatusCode } from "axios"

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

          <Route path="/editUser" element={<EditUserForm />} />
          <Route path="/editEnterprise" element={<EditEnterpriseForm />} />
          <Route path="/imageProfile" element={<ImageProfileForm />} />
          {/*TODO: revisar --> distinto link para enterprise y user */}

          <Route path="/profiles" element={<DiscoverProfiles />} />
          <Route path="/profileEnterprise/:id" element={<ProfileEnterprise />} />
          <Route path="/contactsEnterprise/:id" element={<ContactsEnterprise />} />
          <Route path="/interestedEnterprise/:id" element={<InterestedEnterprise />} />
          <Route path="/contacts" element={<ContactForm />} />
          <Route path="/jobOffers" element={<JobOfferForm />} />

          <Route path="/jobOffer" element={<JobOffer />} />

          <Route path="/jobs" element={<DiscoverJobs />} />
          <Route path="/profileUser/:id" element={<ProfileUser />} />
          <Route path="/notificationsUser/:id" element={<NotificationsUser />} />
          <Route path="/applicationsUser/:id" element={<ApplicationsUser />} />
          <Route path="/skills" element={<SkillsForm />} />
          <Route path="/experiences" element={<ExperienceForm />} />
          <Route path="/educations" element={<EducationForm />} />
          <Route path="/401" element={<Error statusCode={HttpStatusCode.Unauthorized} />} />
          <Route path="/403" element={<Error statusCode={HttpStatusCode.Forbidden} />} />
          <Route path="*" element={<Error statusCode={HttpStatusCode.NotFound} />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App
