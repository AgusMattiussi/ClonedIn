import { renderHook, act } from '@testing-library/react';
import { useGetUserExperiences } from '../../hooks/useGetUserExperiences';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetUserExperiences', () => {
  it('fetches user experiences successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetUserExperiences ());

    // Mock API request response
    const mockResponse = { status: 200, data: [
        {
            "description": "I stop footballs from going into the net",
            "enterpriseName": "AVFC",
            "id": 14,
            "links": {
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28/experiences/14",
                "user": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28"
            },
            "monthFrom": 1,
            "position": "Goalkeeper",
            "yearFrom": 2017
        }
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"

    let getResult;
    await act(async () => {
      getResult = await result.current.getUserExperiences(id);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users/${id}/experiences`,
        method: "GET",
        requiresAuth: true,
    });
    expect(getResult).toEqual(mockResponse);
  });
});