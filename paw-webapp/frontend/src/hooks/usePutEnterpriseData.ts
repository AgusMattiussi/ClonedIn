import { useRequestApi } from "../api/apiRequest"

export const usePutEnterpriseData = () => {
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

  async function closeJobOffer(
    id: string | undefined,
    jobOfferToCloseId: number | null,
    queryParams: Record<string, string> = {},
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}/jobOffers/${jobOfferToCloseId}`,

      method: "PUT",
      queryParams: queryParams,
    })
    return response
  }

  async function answerEnterpriseContact(
    id: string | undefined,
    jobOfferToAnswerId: string,
    userToAnswerId: string,
    queryParams: Record<string, string> = {},
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}/contacts/${jobOfferToAnswerId}/${userToAnswerId}`,
      method: "PUT",
      queryParams: queryParams,
    })
    return response
  }

  return { modifyEnterpriseInfo, closeJobOffer, answerEnterpriseContact }
}
