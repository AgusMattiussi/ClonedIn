import { renderHook, act } from '@testing-library/react';
import { useGetEnterpriseJobOffers } from '../../hooks/useGetEnterpriseJobOffers';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetEnterpriseJobOffers', () => {
  it('fetches enterprise job offers successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetEnterpriseJobOffers ());

    // Mock API request response
    const mockResponse = { status: 200, data: [
        {
            "available": "Activa",
            "description": "Come join the best team in the Premier League",
            "id": 29,
            "links": {
                "category": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/1",
                "enterprise": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/enterprises/29",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers/29",
                "skills": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/skills?jobOfferId=29"
            },
            "modality": "Presencial",
            "position": "Goalkeeper",
            "salary": 1000000.0
        },
        {
            "available": "Activa",
            "description": "Come join the best team in the Premier League",
            "id": 30,
            "links": {
                "category": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/1",
                "enterprise": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/enterprises/30",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers/30",
                "skills": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/skills?jobOfferId=30"
            },
            "modality": "Presencial",
            "position": "CB",
            "salary": 50000.0
        },
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id="29"

    let getResult;
    await act(async () => {
      getResult = await result.current.getEnterpriseJobOffers(id);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
      url: `/jobOffers?enterpriseId=${id}`,
      method: 'GET',
      queryParams: {},
      requiresAuth: true,
    });
    expect(getResult).toEqual(mockResponse);
  });
});