import { renderHook } from "@testing-library/react";
import { useGetUserById } from "../useGetUserById";
import { isEmptyArray } from "formik";

// we will need this mock on our next test
global.fetch = jest.fn();

const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));

describe("useGetUserById", () => {
  it("should return the initial values for data, error and loading", async () => {
    
    const { result } = renderHook(() => useGetUserById());
    const response  = result.current.getUserById("");

    expect(JSON.stringify(response)).toBe('{}')
  });
});