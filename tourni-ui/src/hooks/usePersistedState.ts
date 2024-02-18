import React from 'react';

const usePersistedState = (key: string, defaultValue: unknown) => {
  const [state, setState] = React.useState(() => {
    const persistedState = localStorage.getItem(key);
    return persistedState ? JSON.parse(persistedState) : defaultValue;
  });

  React.useEffect(() => {
    console.log(`setting ${key} to ${state}`);
    localStorage.setItem(key, JSON.stringify(state));
  }, [key, state]);

  return [state, setState];
};

export default usePersistedState;
