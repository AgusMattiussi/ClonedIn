import { useRequestApi } from "../api/apiRequest"

export const usePutEnterpriseInfo = () => {
  const { apiRequest } = useRequestApi()

  async function modifyEnterpriseInfo(
    id: string | undefined,
    name: string,
    aboutUs: string,
    city: string,
    category: string | undefined,
    link: string,
    workers: string | undefined,
    foundingYear: string,
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}`,
      method: "PUT",
      body: {
        name,
        aboutUs,
        city,
        category,
        link,
        workers,
        foundingYear,
      },
    })
    return response
  }

  return { modifyEnterpriseInfo }
}
