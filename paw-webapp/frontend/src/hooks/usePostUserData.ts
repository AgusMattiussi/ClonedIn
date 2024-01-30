import { useRequestApi } from "../api/apiRequest"

export const usePostUserData = () => {
  const { apiRequest } = useRequestApi()

  async function addSkill(id: string | undefined, skill: string) {
    const response = await apiRequest({
      url: `/users/${id}/skills`,
      method: "POST",
      body: {
        skill,
      },
    })
    return response
  }

  async function addExperience(
    id: string | undefined,
    company: string,
    job: string,
    jobDesc: string,
    monthFrom: string,
    yearFrom: string,
    monthTo: string,
    yearTo: string,
  ) {
    const response = await apiRequest({
      url: `/users/${id}/experiences`,
      method: "POST",
      body: {
        company,
        job,
        jobDesc,
        monthFrom,
        yearFrom,
        monthTo,
        yearTo,
      },
    })
    return response
  }

  async function addEducation(
    id: string | undefined,
    college: string,
    degree: string,
    comment: string,
    monthFrom: string,
    yearFrom: string,
    monthTo: string,
    yearTo: string,
  ) {
    const response = await apiRequest({
      url: `/users/${id}/educations`,
      method: "POST",
      body: {
        college,
        degree,
        comment,
        monthFrom,
        yearFrom,
        monthTo,
        yearTo,
      },
    })
    return response
  }

  return { addExperience, addEducation, addSkill }
}
