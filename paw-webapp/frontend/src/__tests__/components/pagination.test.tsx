import { render, screen } from "@testing-library/react";
import Pagination from "../../components/pagination";

let propsMap: { setter: any; pages: string; currentPage: string }

const customPropsMap = (options = {}) => {
  const map = {
    setter: jest.fn(),
    pages: "5",
    currentPage: "1",
  }

  return { ...map, ...options }
}

describe("Test Pagination", () => {
  beforeEach(() => {
    propsMap = customPropsMap()
  })

  test("Test that all props are visible", () => {
    render(<Pagination {...propsMap} />)
    for (let i = 1; i <= Number(propsMap.pages); i++) {
      let page = i.toString()
      expect(screen.getByText(`${page}`)).toBeInTheDocument()
    }
  })
})
