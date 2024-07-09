import { renderHook, act } from '@testing-library/react';
import { useGetCategories } from '../../hooks/useGetCategories';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetCategories', () => {
  it('fetches categories successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetCategories());

    // Mock API request response
    const mockResponse = { status: 200, data: [{
      "id": 2,
      "links": {
          "enterprisesInCategory": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/enterprises?categoryName=Tecnologia",
          "jobOffersInCategory": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers?categoryName=Tecnologia",
          "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/2",
          "usersInCategory": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users?categoryName=Tecnologia"
      },
      "name": "Tecnologia"
      },
      {
        "id": 3,
        "links": {
            "enterprisesInCategory": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/enterprises?categoryName=Medicina",
            "jobOffersInCategory": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers?categoryName=Medicina",
            "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/3",
            "usersInCategory": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users?categoryName=Medicina"
        },
        "name": "Medicina"
      },
  ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    let getResult;
    await act(async () => {
      getResult = await result.current.getCategories();
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
      url: '/categories',
      method: 'GET',
    });
    expect(getResult).toEqual(mockResponse);
  });
});