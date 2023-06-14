import React from "react"
import {screen} from "@testing-library/react"
import {render} from "./test-utils"
import {App} from "./App"

test("simple test to render bookings page", () => {
    render(<App/>)
    const linkElement = screen.getByText(/View your bookings/i)
    expect(linkElement).toBeInTheDocument()
})
