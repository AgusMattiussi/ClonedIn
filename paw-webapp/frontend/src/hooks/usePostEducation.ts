import { useRequestApi } from "../api/apiRequest"

export const usePostEducation = () => {
  const { apiRequest } = useRequestApi()

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

  return { addEducation }
}
