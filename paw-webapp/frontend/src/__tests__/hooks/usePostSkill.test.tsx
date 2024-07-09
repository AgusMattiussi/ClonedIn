import { renderHook, act } from '@testing-library/react';
import { usePostSkill } from '../../hooks/usePostSkill';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('usePostSkill', () => {
  it('creates skill successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostSkill());

    // Mock API request response
    const mockResponse = { status: 400, data: { message: 'Skill created successfully' } };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const description = "Spring Framework"
   
    let postResult;
    await act(async () => {
      postResult = await result.current.addSkill(id, description);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users/${id}/skills`,
        method: "POST",
        body: {
          description,
        },
    });
    expect(postResult).toEqual(mockResponse);
  });

  it('fails to create skill', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostSkill());

    // Mock API request response
    const mockResponse = { status: 208, data: [
        {
            "detail": "may not be empty",
            "errorClass": "ConstraintViolationException",
            "message": "Invalid parameter: description = ."
        }
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const description = ""
   
    let postResult;
    await act(async () => {
      postResult = await result.current.addSkill(id, description);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users/${id}/skills`,
        method: "POST",
        body: {
          description,
        },
    });
    expect(postResult).toEqual(mockResponse);
  });
});