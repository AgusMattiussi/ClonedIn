import { useRequestApi } from "../api/apiRequest"

export const usePostEnterpriseInfo = () => {
  const { apiRequest } = useRequestApi()

  async function modifyEnterpriseInfo(
    id: string | undefined,
    name: string,
    description: string,
    location: string,
    category: string | undefined,
    website: string,
    workers: string | undefined,
    yearFounded: string,
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}`,
      method: "POST",
      body: {
        name,
        description,
        location,
        category,
        website,
        workers,
        yearFounded,
      },
    })
    return response
  }

  return { modifyEnterpriseInfo }
}
