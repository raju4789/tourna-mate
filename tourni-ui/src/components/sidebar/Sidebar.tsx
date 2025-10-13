import * as React from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import TableRowsIcon from '@mui/icons-material/TableRows';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import { AddBox, SportsSoccer, Group } from '@mui/icons-material';
import { useNavigate } from 'react-router';
import { Anchor } from '../../types/Types';
import useLocalStorage from '../../hooks/useLocalStorage';
import { isAdmin } from '../../utils/roleHelpers';

interface ISidebarProps {
  sideBarDirection: { left: boolean; };
  toggleDrawer: (anchor: Anchor, open: boolean) => (event: React.KeyboardEvent | React.MouseEvent) => void;
}

export default function Sidebar(props: ISidebarProps) {
  const { sideBarDirection, toggleDrawer } = props;

  const navigate = useNavigate();

  const { getItem: getRole } = useLocalStorage('role' as string);
  const { getItem: getIsAuthenticated } = useLocalStorage('isAuthenticated' as string);
  
  const userRoles = getRole() as string | null;

  // Use useMemo to prevent recalculation on every render
  const sideBarItems = React.useMemo(() => {
    const items: string[] = ['Points table'];
    
    if (getIsAuthenticated() && isAdmin(userRoles)) {
      items.push('Add match result', 'Add tournament', 'Add team');
    }
    
    return items;
  }, [getIsAuthenticated, userRoles]);

  const handlePointsTableClick = () => {
    navigate('/pointsTable');
  };

  const handleAddMatchResultClick = () => {
    navigate('/addMatchResult');
  };

  const handleAddTournamentClick = () => {
    navigate('/addMatchResult');
  };

  const handleAddTeamClick = () => {
    navigate('/addMatchResult');
  };

  const list = (anchor: Anchor) => (
    <Box
      sx={{ width: anchor === 'top' || anchor === 'bottom' ? 'auto' : 250 }}
      role="presentation"
      onClick={toggleDrawer(anchor, false)}
      onKeyDown={toggleDrawer(anchor, false)}
    >
      <List>
        {sideBarItems.map((text, index) => (
          <ListItem key={text} disablePadding>
            <ListItemButton>
              {(() => {
                switch (index) {
                  case 0:
                    return (
                      <Box onClick={handlePointsTableClick} sx={{ display: 'flex' }}>
                        <TableRowsIcon />
                        <ListItemText primary={text} sx={{ marginLeft: '8px', marginTop: 0 }} />
                      </Box>
                    );

                  case 1:
                    return (
                      <Box onClick={handleAddMatchResultClick} sx={{ display: 'flex' }}>
                        <AddBox />
                        <ListItemText primary={text} sx={{ marginLeft: '8px', marginTop: 0 }} />
                      </Box>
                    );
                  case 2:
                    return (
                      <Box onClick={handleAddTournamentClick} sx={{ display: 'flex' }}>
                        <SportsSoccer />
                        <ListItemText primary={text} sx={{ marginLeft: '8px', marginTop: 0 }} />
                      </Box>
                    );
                  case 3:
                    return (
                      <Box onClick={handleAddTeamClick} sx={{ display: 'flex' }}>
                        <Group />
                        <ListItemText primary={text} sx={{ marginLeft: '8px', marginTop: 0 }} />
                      </Box>
                    );
                  default:
                    return null;
                }
              })()}
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );

  return (
    <div>
      {(['left'] as const).map((anchor) => (
        <React.Fragment key={anchor}>
          <Drawer
            anchor={anchor}
            open={sideBarDirection[anchor]}
            onClose={toggleDrawer(anchor, false)}
          >
            {list(anchor)}
          </Drawer>
        </React.Fragment>
      ))}
    </div>
  );
}
