import { renderHook, act } from '@testing-library/react';
import { usePostEducation } from '../../hooks/usePostEducation';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('usePostEducation', () => {
  it('creates education successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostEducation());

    // Mock API request response
    const mockResponse = { status: 201, data: { message: 'Education created successfully' } };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const college = "Exampleton University"
    const degree = "Example Engeneering Degree"
    const comment = "This is an example education"
    const yearFrom = "2017"
    const monthFrom = "3"
    const yearTo = "2022"
    const monthTo = "12"
   
    let postResult;
    await act(async () => {
      postResult = await result.current.addEducation(id, college, degree, comment, monthFrom, yearFrom, monthTo, yearTo);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
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
    });
    expect(postResult).toEqual(mockResponse);
  });
});