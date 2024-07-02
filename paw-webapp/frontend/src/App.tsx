import "./styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css" // Bootstrap CSS
import "bootstrap/dist/js/bootstrap.bundle.min" // Bootstrap Bundle JS
import React, { Suspense, lazy } from "react"
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"
import { HttpStatusCode } from "axios"
import { useSharedAuth } from "./api/auth"
import Loading from "./views/loading"

const Login = lazy(() => import("./views/login"))
const DiscoverJobs = lazy(() => import("./views/discoverJobs"))
const DiscoverProfiles = lazy(() => import("./views/discoverProfiles"))
const RegisterEnterprise = lazy(() => import("./views/enterpriseRegister"))
const EditEnterpriseForm = lazy(() => import("./views/enterpriseEditForm"))
const ContactForm = lazy(() => import("./views/enterpriseContactForm"))
const JobOfferForm = lazy(() => import("./views/enterpriseJobOfferForm"))
const ImageProfileForm = lazy(() => import("./views/imageProfileForm"))
const RegisterUser = lazy(() => import("./views/userRegister"))
const EditUserForm = lazy(() => import("./views/userEditForm"))
const SkillsForm = lazy(() => import("./views/userSkillsForm"))
const ExperienceForm = lazy(() => import("./views/userExperienceForm"))
const EducationForm = lazy(() => import("./views/userEducationForm"))
const ProfileUser = lazy(() => import("./views/userProfile"))
const NotificationsUser = lazy(() => import("./views/userNotifications"))
const ApplicationsUser = lazy(() => import("./views/userApplications"))
const ProfileEnterprise = lazy(() => import("./views/enterpriseProfile"))
const ContactsEnterprise = lazy(() => import("./views/enterpriseContacts"))
const InterestedEnterprise = lazy(() => import("./views/enterpriseInterested"))
const JobOffer = lazy(() => import("./views/jobOffer"))
const Error = lazy(() => import("./views/error"))

function App() {
  const { userInfo } = useSharedAuth()

  return (
    <div className="App">
      <BrowserRouter>
        <Suspense fallback={<Loading />}>
          <Routes>
            <Route path="/login" element={<Login />} />
            {userInfo === null ? (
              <Route path="/" element={<Navigate to="/login" />} />
            ) : userInfo?.role === "ENTERPRISE" ? (
              <Route path="/" element={<Navigate to="/users" />} />
            ) : (
              <Route path="/" element={<Navigate to="/jobOffers" />} />
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
        </Suspense>
      </BrowserRouter>
    </div>
  )
}

export default App
