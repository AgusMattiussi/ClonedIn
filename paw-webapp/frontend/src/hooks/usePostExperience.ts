import { useRequestApi } from "../api/apiRequest"

export const usePostExperience = () => {
  const { apiRequest } = useRequestApi()

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

  return { addExperience }
}
