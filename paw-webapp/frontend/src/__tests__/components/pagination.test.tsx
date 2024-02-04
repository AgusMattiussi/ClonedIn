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
    const pages = propsMap.pages
    pages.localeCompare((num) => {
      expect(screen.getByText(`${num}`)).toBeInTheDocument()
    })
  })
})
