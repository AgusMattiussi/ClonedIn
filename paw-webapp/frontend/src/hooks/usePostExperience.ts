import { useRequestApi } from "../api/apiRequest"

export const usePostExperience = () => {
  const { apiRequest } = useRequestApi()

  async function addExperience(
    id: string | undefined,
    enterpriseName: string,
    position: string,
    description: string,
    monthFrom: string,
    yearFrom: string,
    monthTo: string,
    yearTo: string,
  ) {
    const response = await apiRequest({
      url: `/users/${id}/experiences`,
      method: "POST",
      body: {
        enterpriseName,
        position,
        description,
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
