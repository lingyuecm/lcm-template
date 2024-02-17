import {useEffect, useState} from "react";
import {getRolesApi} from "../../api/roleApi";
import {createTheme, Pagination, Table, TableBody, TableCell, TableHead, TableRow, ThemeProvider} from "@mui/material";
import {
    colorBlue,
    colorBlueDark,
    colorGreyD2,
    colorOrange,
    colorOrangeDark,
    colorOrangeDarker
} from "../../utils/constant";
import styled from "styled-components";

const RolePermissionButton = styled.div`
    display: inline-block;
    padding: 0.3rem 1rem;
    border-radius: 1rem;
    background-color: ${() => colorOrange};
    box-shadow: 0 0 0.2rem #333333;
    &:hover {
        cursor: pointer;
        background-color: ${() => colorOrangeDark}
    }
    &:active {
        background-color: ${() => colorOrangeDarker}
    }
`

const RoleTotalCount = styled.span`
    float: right;
    margin-top: 1rem;
    font-size: 1.5rem;
    padding: 0.6rem 0;
`

const roleTableTheme = createTheme({
    components: {
        MuiTableHead: {
            styleOverrides: {
                root: {
                    backgroundColor: colorBlue
                }
            }
        },
        MuiTableCell: {
            styleOverrides: {
                head: {
                    fontSize: "1.8rem"
                }
            }
        },
        MuiTableBody: {
            styleOverrides: {
                root: {
                    borderLeft: "1px solid " + colorGreyD2,
                    borderRight: "1px solid " + colorGreyD2
                }
            }
        },
        MuiPaginationItem: {
            styleOverrides: {
                root: {
                    fontSize: "1.5rem",
                    "&.Mui-selected": {
                        backgroundColor: colorBlue,
                        color: colorOrange,
                        fontWeight: 700
                    },
                    "&.Mui-selected:hover": {
                        backgroundColor: colorBlueDark
                    }
                },
                icon: {
                    width: "2rem",
                    height: "2rem"
                }
            }
        }
    }
});

export default function RoleManagement() {
    const [roles, setRoles] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [pages, setPages] = useState(0);
    const [pageNo, setPageNo] = useState(1);
    let pageSize = 10;

    function searchForRoles() {
        const params = {};
        params.pageNo = pageNo;
        params.pageSize = pageSize;

        getRolesApi(params)
            .then(response => {
                setTotalCount(response["resultBody"]["totalCount"]);
                setPages(Math.floor((response["resultBody"]["totalCount"] + pageSize - 1) / pageSize));
                setRoles(response["resultBody"]["dataList"]);
            }).catch(() => {});
    }

    useEffect(() => {
        searchForRoles();
    }, [pageNo]);
    return <div>
        <ThemeProvider theme={roleTableTheme}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell style={{borderTopLeftRadius: "1rem", borderRight: "1px solid #B2B2B2"}}
                                   align={"center"}>Name</TableCell>
                        <TableCell style={{borderTopRightRadius: "1rem"}} align={"center"}>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {roles.map((role, index) => {
                        return <TableRow key={"RoleRow-" + index}>
                            <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid " + colorGreyD2}} align={"center"}>{role.roleName}</TableCell>
                            <TableCell style={{fontSize: "1.5rem"}} align={"center"}>
                                <RolePermissionButton>Permissions</RolePermissionButton>
                            </TableCell>
                        </TableRow>;
                    })}
                </TableBody>
            </Table>
            <Pagination style={{float: "right", marginTop: "1rem"}} count={pages} page={pageNo}
                        onChange={(e, page) => {setPageNo(page)}}/>
            <RoleTotalCount>{totalCount} in total</RoleTotalCount>
        </ThemeProvider>
    </div>
}